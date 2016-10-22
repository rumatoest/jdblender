package dbshaker.core;

import dbshaker.core.testers.BrandsTester;
import dbshaker.core.testers.ModelsTester;
import dbshaker.core.testers.SeriesTester;
import dbshaker.core.testers.SparesTester;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.math.BigDecimal;
import java.math.RoundingMode;
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
public class ShakerApp {

    private static final Logger LOG = Logger.getLogger(ShakerApp.class.getCanonicalName());

    private static final String SQL_SCHEMA = "/dbshaker/db-schema.sql";

    private static final String SQL_BRANDS_IDX = "/dbshaker/db-brands-idx.sql";

    private static final String SQL_SERIES_IDX = "/dbshaker/db-series-idx.sql";

    private static final String SQL_MODELS_IDX = "/dbshaker/db-models-idx.sql";

    private static final String SQL_SPARES_IDX = "/dbshaker/db-spares-idx.sql";

    Connection connection;

    FrameworkRunner runner;

    ArrayList<Scores> scores = new ArrayList<Scores>();

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
        try {
            connection = connect();
            dbInit();
            runner.init(new DbConnection());

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
        System.err.print(scoresHeader());
        System.err.print(scores());
        System.exit(0);
    }

    void runInsertTests() throws Exception {
        tstBrands = new BrandsTester(runner);
        runTestsOn("brands IN", tstBrands::insertData);

        dbAddBrandsIndexes();

        tstSeries = new SeriesTester(runner);
        runTestsOn("series IN", tstSeries::insertData);

        dbAddSeriesIndexes();

        tstModels = new ModelsTester(runner);
        runTestsOn("models IN", tstModels::insertData);

        dbAddModelsIndexes();

        tstSpares = new SparesTester(runner);
        runTestsOn("spares IN", tstSpares::insertData);

        dbAddSparesIndexes();
    }

    void runSelectTests() throws Exception {
        runTestsOn("brands SEL", tstBrands::selectSome);
        runTestsOn("series SEL", tstSeries::selectSome);
        runTestsOn("series SELO", tstSeries::selectSomeObj);
        runTestsOn("models SEL", tstModels::selectSome);
        runTestsOn("models SELO", tstModels::selectSomeObj);
        runTestsOn("spares SEL", tstSpares::selectSome);
        runTestsOn("spares SELO", tstSpares::selectSomeObj);
    }

    void runTestsOn(String label, CallbackWithScores callback) throws Exception {
        Scores s = callback.run();
        s.label(label);
        this.scores.add(s);
    }

    String scoresHeader() {
        long code = 0L;
        StringBuilder sb = new StringBuilder();
        for (Scores s : this.scores) {
            code = code / 2 + s.getCode() / 2;
            if (sb.length() > 0) {
                sb.append(',');
            }
//            sb.append(s.label()).append("[num runs],");
//            sb.append(s.label()).append("[time sec],");
            sb.append(s.label()).append("[avg mu]");
        }

        System.out.println("code: " + code);
        return sb.append("\n").toString();
    }

    String scores() {
        StringBuilder sb = new StringBuilder();
        for (Scores s : this.scores) {
            if (sb.length() > 0) {
                sb.append(',');
            }

//            sb.append(s.iterations()).append(',');
//            sb.append(s.timeSec()).append(',');
//            sb.append(BigDecimal.valueOf(s.timeSec()).setScale(2, RoundingMode.HALF_UP)).append(',');
            sb.append(BigDecimal.valueOf(s.avgMu()).setScale(6, RoundingMode.HALF_UP));
        }
        return sb.append("\n").toString();
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
            st.execute(ShakerApp.getResourceAsString(SQL_SCHEMA));
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

    void dbAddBrandsIndexes() throws Exception {
        try (Statement st = connection.createStatement()) {
            LOG.log(Level.INFO, "Adding PK for brands");
            st.execute(ShakerApp.getResourceAsString(SQL_BRANDS_IDX));
            connection.commit();
        }
    }

    void dbAddSeriesIndexes() throws Exception {
        try (Statement st = connection.createStatement()) {
            LOG.log(Level.INFO, "Adding PK for series");
            st.execute(ShakerApp.getResourceAsString(SQL_SERIES_IDX));
            connection.commit();
        }
    }

    void dbAddModelsIndexes() throws Exception {
        try (Statement st = connection.createStatement()) {
            LOG.log(Level.INFO, "Adding PK for models");
            st.execute(ShakerApp.getResourceAsString(SQL_MODELS_IDX));
            connection.commit();
        }
    }

    void dbAddSparesIndexes() throws Exception {
        try (Statement st = connection.createStatement()) {
            LOG.log(Level.INFO, "Adding PK for spares");
            st.execute(ShakerApp.getResourceAsString(SQL_SPARES_IDX));
            connection.commit();
        }
    }

    public static String getResourceAsString(String resource) throws IOException {
        String query;
        try (final InputStream is = ShakerApp.class.getResourceAsStream(resource)) {

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
