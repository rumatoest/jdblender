package dbshaker.jdbc;

import dbshaker.core.DbConnection;
import dbshaker.core.FrameworkRunner;
import dbshaker.jdbc.dao.Brand;
import dbshaker.jdbc.dao.Model;
import dbshaker.jdbc.dao.Series;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

/**
 *
 */
public class RunnerJdbc implements FrameworkRunner {

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
    public Brand getBrand(long id) throws Exception {
        try (PreparedStatement ps = connect.prepareStatement("SELECT id, name FROM brands WHERE id = ?")) {
            ps.setLong(1, id);
            ResultSet rs = ps.executeQuery();
            if (!rs.next()) {
                return null;
            }

            Brand brand = new Brand();
            brand.setId(rs.getLong(1));
            brand.setName(rs.getString(2));
            rs.close();
            return brand;
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
    public Series getSeries(long id) throws Exception {
        try (PreparedStatement ps = connect.prepareStatement("SELECT id, brand_id, name FROM series WHERE id = ?")) {
            ps.setLong(1, id);
            ResultSet rs = ps.executeQuery();
            if (!rs.next()) {
                return null;
            }

            Series obj = new Series();
            obj.setId(rs.getLong(1));
            obj.setBrandId(rs.getLong(2));
            obj.setName(rs.getString(3));
            rs.close();
            return obj;
        }
    }

    @Override
    public Series getSeriesObj(long id) throws Exception {
        try (PreparedStatement ps = connect.prepareStatement(
            "SELECT s.id, s.name, s.brand_id, b.name brand_name FROM series s "
            + "INNER JOIN brands b ON b.id = s.brand_id WHERE s.id = ?"
        )) {
            ps.setLong(1, id);
            ResultSet rs = ps.executeQuery();
            if (!rs.next()) {
                return null;
            }

            Brand jobj = new Brand();
            jobj.setId(rs.getLong(3));
            jobj.setName(rs.getString(4));

            Series obj = new Series();
            obj.setId(rs.getLong(1));
            obj.setName(rs.getString(2));
            obj.setBrand(jobj);
            rs.close();
            return obj;
        }
    }

    @Override
    public void createModel(long id, long seriesId, String name) throws Exception {
        try (PreparedStatement ps = connect.prepareStatement("INSERT INTO models (id, series_id, name) VALUES (?,?,?)")) {
            ps.setLong(1, id);
            ps.setLong(2, seriesId);
            ps.setString(3, name);
            ps.executeUpdate();
            connect.commit();
        }
    }

    @Override
    public Model getModel(long id) throws Exception {
        try (PreparedStatement ps = connect.prepareStatement("SELECT id, series_id, name FROM models WHERE id = ?")) {
            ps.setLong(1, id);
            ResultSet rs = ps.executeQuery();
            if (!rs.next()) {
                return null;
            }

            Model obj = new Model();
            obj.setId(rs.getLong(1));
            obj.setSeriesId(rs.getLong(2));
            obj.setName(rs.getString(3));
            rs.close();
            return obj;
        }
    }

    @Override
    public Model getModelObj(long id) throws Exception {
        try (PreparedStatement ps = connect.prepareStatement(
            "SELECT m.id, m.name, s.id s_id, s.name s_name, b.id b_id, b.name b_name FROM models m "
                + "INNER JOIN series s ON s.id = m.series_id "
                + "INNER JOIN brands b ON b.id = s.brand_id "
                + "WHERE m.id = ?"
        )) {
            ps.setLong(1, id);
            ResultSet rs = ps.executeQuery();
            if (!rs.next()) {
                return null;
            }
            
            
            Brand bojb = new Brand();
            bojb.setId(rs.getLong(5));
            bojb.setName(rs.getString(6));
            
            Series sobj = new Series();
            sobj.setBrand(bojb);
            sobj.setId(rs.getLong(3));
            sobj.setName(rs.getString(4));

            Model obj = new Model();
            obj.setSeries(sobj);
            obj.setId(rs.getLong(1));
            obj.setName(rs.getString(2));
            rs.close();
            return obj;
        }
    }

    @Override
    public void createSpare(long id, String name) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void linkSpare2ModelVariant(long spareId, long modelVariantId) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
