package jdblender.core;

import jdblender.core.testers.BrandsTester;
import jdblender.core.testers.ModelsTester;
import jdblender.core.testers.SeriesTester;
import jdblender.core.testers.SparesTester;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.lang3.math.NumberUtils;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Main test application runner.
 */
public class BlenderApp {
    
    private static final Logger LOG = Logger.getLogger(BlenderApp.class.getCanonicalName());
    
    private static final String SQL_SCHEMA = "/dbshaker/db-schema.sql";
    
    private static final String SQL_BRANDS_IDX = "/dbshaker/db-brands-idx.sql";
    
    private static final String SQL_SERIES_IDX = "/dbshaker/db-series-idx.sql";
    
    private static final String SQL_MODELS_IDX = "/dbshaker/db-models-idx.sql";
    
    private static final String SQL_SPARES_IDX = "/dbshaker/db-spares-idx.sql";
    
    private static final String SQL_M2M_IDX = "/dbshaker/db-m2m-idx.sql";
    
    private int factor = 1;
    
    private boolean noJit;
    
    Connection connection;
    
    FrameworkRunner runner;
    
    ArrayList<Scores> scores = new ArrayList<>();
    
    BrandsTester tstBrands;
    
    SeriesTester tstSeries;
    
    ModelsTester tstModels;
    
    SparesTester tstSpares;

    /**
     * All you have to do it instantiate this class and call this method.
     *
     * @param runner Runner implementation
     * @param args Command line arguments
     */
    public void main(FrameworkRunner runner, String[] args) throws Exception {
        noJit = "NONE".equals(System.getenv("JIT"));
        factor = runner.getFactor();
        
        LOG.info("JIT " + (noJit ? "(#_#) DISABLED" : "(^_^) ENABLED"));
        
        Path reportFile = null;
        if (args.length < 1) {
            LOG.warning("CSV output not defined!!");
        } else {
            reportFile = Paths.get(args[0]);
        }
        
        if (args.length > 1) {
            configureFromArgs(args[1].split(" "));
        }
        
        this.runner = runner;
        try {
            connection = connect();
            dbInit();
            runner.init(new DbConnection());
            
            dbHeat();
            runInsertTests();
            runSelectTests();
            
        } catch (Exception ex) {
            LOG.log(Level.SEVERE, ex.getMessage(), ex);
        } finally {
            if (connection != null || !connection.isClosed()) {
                connection.close();
                runner.close();
            }
        }
        
        System.out.flush();
        System.err.flush();
        
        if (reportFile != null) {
            ReportWriter.write(reportFile, scores, getFactor());
        } else {
            System.err.append("CSV output not defined!!");
        }
        System.exit(0);
    }
    
    public void configureFromArgs(String[] args) {
        //System.out.println("ARGS: " + StringUtils.join(args, " "));
        Options options = new Options();
        options.addOption("f", true, "Test set factor (default 1) -> tests_count / factor ");
        
        CommandLineParser parser = new DefaultParser();
        try {
            CommandLine parse = parser.parse(options, args);

            // FACTOR
            if (parse.hasOption('f')) {
                this.factor = NumberUtils.toInt(parse.getOptionValue('f'), getFactor());
                if (factor > 100) {
                    factor = 100;
                }
                if (factor < 1) {
                    factor = 1;
                }
            }
        } catch (ParseException ex) {
            System.out.println("!!!");
            System.out.println("ERROR occured: " + ex.getMessage());
            System.out.println("!!!");
            HelpFormatter formatter = new HelpFormatter();
            formatter.printHelp("JDBSHAKER", options);
            System.exit(1);
        }
        
        System.out.println("FACTOR of test set = " + getFactor());
    }
    
    public int getFactor() {
        return factor;
    }
    
    void runInsertTests() throws Exception {
        tstBrands = new BrandsTester(this, runner);
        runTestsOn("brands IN", tstBrands::insertData);
        
        LOG.log(Level.INFO, "Adding PK for brands");
        executeSql(SQL_BRANDS_IDX);
        
        tstSeries = new SeriesTester(this, runner, tstBrands);
        runTestsOn("series IN", tstSeries::insertData);
        
        LOG.log(Level.INFO, "Adding PK for series");
        executeSql(SQL_SERIES_IDX);
        
        tstModels = new ModelsTester(this, runner, tstSeries);
        runTestsOn("models IN", tstModels::insertData);
        
        LOG.log(Level.INFO, "Adding PK for models");
        executeSql(SQL_MODELS_IDX);
        
        tstSpares = new SparesTester(this, runner, tstBrands, tstModels);
        runTestsOn("spares IN", tstSpares::insertData);
        
        LOG.log(Level.INFO, "Adding PK for spares");
        executeSql(SQL_SPARES_IDX);
        
        runTestsOn("m2m IN", tstSpares::insertLinks);
        runTestsOn("m2m O IN", tstSpares::insertLinksFast);
        LOG.log(Level.INFO, "Adding PK for m2m links");
        executeSql(SQL_M2M_IDX);
    }
    
    void runSelectTests() throws Exception {
        runTestsOn("brands SEL", tstBrands::selectSome);
        runTestsOn("series SEL", tstSeries::selectSome);
        runTestsOn("series SELO", tstSeries::selectSomeObj);
        runTestsOn("models SEL", tstModels::selectSome);
        runTestsOn("models SELO", tstModels::selectSomeObj);
        runTestsOn("spares SEL", tstSpares::selectSome);
        runTestsOn("spares SELO", tstSpares::selectSomeObj);
        runTestsOn("model+spares SELO", tstSpares::selectSomeObjWithLinks);
        runTestsOn("spares DYN", tstSpares::selectSpares);
    }
    
    void runTestsOn(String label, CallbackWithScores callback) throws Exception {
        Scores s = callback.run();
        s.label(label);
        this.scores.add(s);
    }
    
    Connection connect() throws Exception {
        Class.forName("org.h2.Driver");
        Connection cnx = DriverManager.getConnection(DbConnection.uri, DbConnection.username, DbConnection.password);
        cnx.setAutoCommit(false);
        return cnx;
    }
    
    void dbInit() throws Exception {
        try (Statement st = connection.createStatement()) {
            LOG.log(Level.INFO, "Initializing schema");
            st.execute(BlenderApp.getResourceAsString(SQL_SCHEMA));
            connection.commit();
        }
        try (Statement st = connection.createStatement()) {
            ResultSet rs = st.executeQuery("SHOW TABLES");
            String msg = "H2 tables created:";
            while (rs.next()) {
                msg += "\n  -" + rs.getString(1);
            }
            LOG.log(Level.INFO, msg);
        }
        runner.init(new DbConnection());
    }
    
    void dbHeat() throws Exception {
        LOG.log(Level.INFO, "DB HEAT UP BEGIN");
        try (Statement st = connection.createStatement()) {
            st.execute("CREATE MEMORY TABLE heat ( id BIGINT, name INT)");
            connection.commit();
            
            int upTo = noJit ? 50_001 : 1_000_001;
            
            for (int i = 0; i < upTo; i++) {
                st.execute("INSERT INTO heat VALUES(" + i + ", " + (10_000 - i) + ")");
                if (i % 100 == 0) {
                    connection.commit();
                }
            }
            
            st.execute("DROP TABLE heat");
            connection.commit();
        }
        LOG.log(Level.INFO, "COMPLETE DB HEAT UP");
    }
    
    void executeSql(String fileResource) throws Exception {
        try (Statement st = connection.createStatement()) {
            st.execute(BlenderApp.getResourceAsString(fileResource));
            connection.commit();
        }
    }
    
    public static String getResourceAsString(String resource) throws IOException {
        String query;
        try (final InputStream is = BlenderApp.class.getResourceAsStream(resource)) {
            
            StringWriter stringWriter = new StringWriter();
            int b;
            while ((b = is.read()) != -1) {
                stringWriter.write(b);
            }
            query = stringWriter.toString();
        }
        return query;
    }
}
