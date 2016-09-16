package dbshaker.core;

import dbshaker.core.data.BrandsData;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.logging.Level;

/**
 * Main test application runner.
 */
public class ShakerApp {

    private static final String SQL_SCHEMA = "/dbshaker/schema.sql";

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
            connection.setAutoCommit(false);
            dbInit();
            runner.init(new DbConnection());
            runInsertTests();
            dbAddIndexes();
        } catch (Exception ex) {
            java.util.logging.Logger.getLogger("ShakerApp").log(Level.SEVERE, ex.getMessage(), ex);
        } finally {
            if (connection != null || !connection.isClosed()) {
                connection.close();
                runner.close();
            }
        }
    }

    void runInsertTests() throws Exception {
        Scores res;

        BrandsData brands = new BrandsData(runner);
        res = brands.insertAll();
        brands.clear();
        res.label("brands");
        this.scores.add(res);

        System.err.print(scoresHeader());
        System.err.print(scores());
    }

    String scoresHeader() {
        StringBuilder sb = new StringBuilder();
        for (Scores s : this.scores) {
            if (sb.length() > 0) {
                sb.append(',');
            }
            sb.append(s.label()).append("[n],");
            sb.append(s.label()).append("[time],");
            sb.append(s.label()).append("[avg]");
        }
        return sb.append("\n").toString();
    }

    String scores() {
        StringBuilder sb = new StringBuilder();
        for (Scores s : this.scores) {
            if (sb.length() > 0) {
                sb.append(',');
            }
            
            sb.append(s.iterations()).append(',');
            sb.append(s.timeMs()).append(',');
            sb.append(BigDecimal.valueOf(s.avg()).setScale(6, RoundingMode.HALF_UP));
        }
        return sb.append("\n").toString();
    }

    Connection connect() throws Exception {
        Class.forName("org.h2.Driver");
        return DriverManager.getConnection(DbConnection.uri, DbConnection.username, DbConnection.password);
    }

    void dbInit() throws SQLException, IOException {
        try (Statement st = connection.createStatement()) {
            st.execute(ShakerApp.getResourceAsString(SQL_SCHEMA));
            connection.commit();
        }
        try (Statement st = connection.createStatement()) {
            ResultSet rs = st.executeQuery("SHOW TABLES");
            System.out.println("H2 tables created:");
            while (rs.next()) {
                System.out.println("    -" + rs.getString(1));
            }
        }
        runner.init(new DbConnection());
    }

    void dbAddIndexes() {

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
