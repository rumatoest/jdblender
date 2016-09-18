package dbshaker.jdbc;

import dbshaker.core.DbConnection;
import dbshaker.core.FrameworkRunner;
import dbshaker.core.domain.Brand;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.Statement;

/**
 *
 */
public class Runner implements FrameworkRunner {

    Connection connect;

    @Override
    public void init(DbConnection connection) throws Exception {
        this.connect = connect(connection);
    }

    Connection connect(DbConnection conn) throws Exception {
        Class.forName(conn.driverClass);
        Connection cnx = DriverManager.getConnection(conn.uri, conn.username, conn.password);
        cnx.setAutoCommit(false);
        return cnx;
    }

    @Override
    public void close() throws Exception {
        if (connect != null && !connect.isClosed()) {
            connect.close();
        }
    }

    @Override
    public void createBrand(long id, String name) throws Exception {
        try (PreparedStatement ps = connect.prepareStatement("INSERT INTO brands (id,name) VALUES (?,?)")) {
            ps.setLong(1, id);
            ps.setString(2, name);
            ps.executeUpdate();
            connect.commit();
        }
    }

    @Override
    public void createSeries(long id, long brandId, String name) throws Exception {
        try (PreparedStatement ps = connect.prepareStatement("INSERT INTO series (id, brand_id, name) VALUES (?,?,?)")) {
            ps.setLong(1, id);
            ps.setLong(2, brandId);
            ps.setString(3, name);
            ps.executeUpdate();
            connect.commit();
        }
    }

    @Override
    public void createModel(long id, long brandId, String name) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void createModelVariant(long id, long modelId, String name) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void createSpare(long id, String name) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void linkSpare2ModelVariant(long spareId, long modelVariantId) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Brand getBrand(long id) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
