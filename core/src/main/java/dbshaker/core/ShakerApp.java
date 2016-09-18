package dbshaker.core;

import dbshaker.core.testers.BrandsTester;
import dbshaker.core.testers.SeriesTester;

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

    Connection connection;

    FrameworkRunner runner;

    ArrayList<Scores> scores = new ArrayList<Scores>();

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

        } catch (Exception ex) {
            LOG.log(Level.SEVERE, ex.getMessage(), ex);
        } finally {
            if (connection != null || !connection.isClosed()) {
                connection.close();
                runner.close();
            }
        }
    }

    void runInsertTests() throws Exception {
        Scores res;

        BrandsTester brands = new BrandsTester(runner);
        LOG.log(Level.INFO, "Insert brands");
        runInsertOn("brands IN", brands::insertData);
        brands.clear();

        dbAddBrandsIndexes();

        SeriesTester series = new SeriesTester(runner);
        LOG.log(Level.INFO, "Insert series");
        runInsertOn("series IN", series::insertData);
        
        dbAddSeriesIndexes();

        System.err.flush();
        System.err.print(scoresHeader());
        System.err.print(scores());
    }

    void runInsertOn(String label, CallbackWithScores callback) throws Exception {
        Scores s = callback.run();
        s.label(label);
        this.scores.add(s);
    }

    String scoresHeader() {
        StringBuilder sb = new StringBuilder();
        for (Scores s : this.scores) {
            if (sb.length() > 0) {
                sb.append(',');
            }
//            sb.append(s.label()).append("[num runs],");
//            sb.append(s.label()).append("[time sec],");
            sb.append(s.label()).append("[avg mu]");
        }
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
