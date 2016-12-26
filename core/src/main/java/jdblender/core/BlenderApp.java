package jdblender.core;

import jdblender.core.testers.BrandsTester;
import jdblender.core.testers.ModelsTester;
import jdblender.core.testers.SeriesTester;
import jdblender.core.testers.SparesTester;

import org.apache.commons.lang3.StringUtils;
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

    private Path reportFile;

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
        this.runner = runner;

        configure(args);

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

    /**
     * Arguments:
     * - Path to output report file
     * - Test data set reduce value (default - 1)
     */
    public void configure(String[] args) {
        factor = runner.getFactor();

        noJit = "FALSE".equals(System.getenv("JDB_JIT"));
        if (noJit) {
            LOG.info("JIT (#_#) DISABLED");
        }
        
        //"TRUE".equals(System.getenv("JDB_PROFILE"));

        // Set up output CSV path
        if (args.length > 0 && StringUtils.isNotBlank(args[0])) {
            reportFile = Paths.get(args[0]);
        } else {
            LOG.warning("CSV output not defined!!");
        }

        // Set up reduce value
        if (args.length > 1 && StringUtils.isNotBlank(args[1])) {
            int r = NumberUtils.toInt(args[1], 1);
            if (r > 1) {
                factor = r;
            }
            LOG.info("TEST SET REDUCE FACTOR IS " + factor);
        }

        if (args.length > 2 && StringUtils.isNotBlank(args[2])) {
            LOG.info("FLAGS: " + args[2]);
        }
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
