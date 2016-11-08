package jdblender.jdbc;

import jdblender.core.DbConnection;
import jdblender.core.FrameworkRunner;
import jdblender.core.domain.ModelObj;
import jdblender.jdbc.dao.Brand;
import jdblender.jdbc.dao.Model;
import jdblender.jdbc.dao.Series;
import jdblender.jdbc.dao.Spare;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;

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
    public void createSpare(long id, long brandId, String name, String label, boolean flag, int num) throws Exception {
        try (PreparedStatement ps = connect.prepareStatement(
            "INSERT INTO spares (id, brand_id, name, label, flag, num) VALUES (?,?,?,?,?,?)")) {

            ps.setLong(1, id);
            ps.setLong(2, brandId);
            ps.setString(3, name);
            ps.setString(4, label);
            ps.setBoolean(5, flag);
            ps.setInt(6, num);
            ps.executeUpdate();
            connect.commit();
        }
    }

    @Override
    public Spare getSpare(long id) throws Exception {
        try (PreparedStatement ps = connect.prepareStatement(
            "SELECT s.id, s.name, s.label, s.flag, s.num, s.brand_id FROM spares s WHERE s.id = ?"
        )) {
            ps.setLong(1, id);
            ResultSet rs = ps.executeQuery();
            if (!rs.next()) {
                return null;
            }

            Spare spare = resultToSpare(rs);
            rs.close();
            return spare;
        }
    }

    Spare resultToSpare(ResultSet rs) throws SQLException {
        Spare spare = new Spare();
        spare.setId(rs.getLong(1));
        spare.setName(rs.getString(2));
        spare.setLabel(rs.getString(3));
        spare.setFlag(rs.getBoolean(4));
        spare.setNum(rs.getInt(5));
        spare.setBrandId(rs.getLong(6));
        return spare;
    }

    @Override
    public Spare getSpareObj(long id) throws Exception {
        try (PreparedStatement ps = connect.prepareStatement(
            "SELECT s.id, s.name, s.label, s.flag, s.num, s.brand_id, b.name brand_name FROM spares s "
            + "INNER JOIN brands b ON b.id = s.brand_id WHERE s.id = ?"
        )) {
            ps.setLong(1, id);
            ResultSet rs = ps.executeQuery();
            if (!rs.next()) {
                return null;
            }

            Spare spare = resultToSpareObj(rs);

            rs.close();
            return spare;
        }
    }

    Spare resultToSpareObj(ResultSet rs) throws SQLException {
        Brand jobj = new Brand();
        jobj.setId(rs.getLong(6));
        jobj.setName(rs.getString(7));

        Spare spare = new Spare();
        spare.setId(rs.getLong(1));
        spare.setName(rs.getString(2));
        spare.setLabel(rs.getString(3));
        spare.setFlag(rs.getBoolean(4));
        spare.setNum(rs.getInt(5));
        spare.setBrand(jobj);
        return spare;
    }

    @Override
    public void linkModel2Spare(long modelId, long spareId) throws Exception {
        try (PreparedStatement ps = connect.prepareStatement(
            "INSERT INTO spare_to_model (spare_id, model_id) VALUES (?,?)")) {

            ps.setLong(1, spareId);
            ps.setLong(2, modelId);
            ps.executeUpdate();
            connect.commit();
        }
    }

    @Override
    public void linkModel2SpareOptimized(long modelId, long spareId) throws Exception {
        linkModel2Spare(modelId, spareId);
    }

    @Override
    public ModelObj getModelObjWithSpares(long id) throws Exception {
        Model model = getModelObj(id);

        try (PreparedStatement ps = connect.prepareStatement(
            "SELECT s.id, s.name, s.label, s.flag, s.num, s.brand_id, b.name brand_name FROM spares s "
            + "INNER JOIN brands b ON b.id = s.brand_id "
            + "INNER JOIN spare_to_model m2m ON m2m.spare_id = s.id "
            + "WHERE m2m.model_id = ?"
        )) {
            ps.setLong(1, id);
            ResultSet rs = ps.executeQuery();

            HashSet<Spare> spares = new HashSet<>();
            while (rs.next()) {
                spares.add(resultToSpareObj(rs));
            }
            rs.close();
            model.setSpares(spares);
        }
        return model;
    }

    @Override
    public Collection<Spare> getSpares(String label, Boolean flag,
        Integer numFromInclusive, Integer numToInclusive) throws Exception {

        ArrayList params = new ArrayList();
        StringBuilder sb = new StringBuilder("SELECT s.id, s.name, s.label, s.flag, s.num, s.brand_id "
            + "FROM spares s WHERE ");

        if (label != null) {
            sb.append(" s.label = ? ");
            params.add(label);
        }

        if (flag != null) {
            if (!params.isEmpty()) {
                sb.append(" AND ");
            }
            sb.append(" s.flag = ? ");
            params.add(flag);
        }

        if (numFromInclusive != null) {
            if (!params.isEmpty()) {
                sb.append(" AND ");
            }

            if (numToInclusive == null) {
                sb.append("s.num = ?");
                params.add(numFromInclusive);
            } else {
                sb.append("s.num >= ? AND s.num <= ?");
                params.add(numFromInclusive);
                params.add(numToInclusive);
            }
        }

        try (PreparedStatement ps = connect.prepareStatement(sb.toString())) {
            for (int i = 0; i < params.size(); i++) {
                ps.setObject(i + 1, params.get(i));
            }

            ResultSet rs = ps.executeQuery();
            ArrayList<Spare> spares = new ArrayList<>();
            while (rs.next()) {
                spares.add(resultToSpare(rs));
            }
            return spares;
        }
    }

}
