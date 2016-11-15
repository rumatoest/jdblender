package jdblender.sjdbc.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import jdblender.sjdbc.model.Brand;
import jdblender.sjdbc.model.Series;
import org.springframework.stereotype.Repository;

@Repository
public class SeriesDao extends CommonDao {

    public void create(long id, long brandId, String name) {
        template().update("INSERT INTO series (id, brand_id, name) VALUES (?,?,?)", id, brandId, name);
    }

    public Series get(long id) {
        return template().queryForObject("SELECT id, brand_id, name FROM series WHERE id = ?",
            this::map, id);
    }

    public Series getObj(long id) {
        return template().queryForObject("SELECT s.id, s.name, s.brand_id, b.name brand_name FROM series s "
            + "INNER JOIN brands b ON b.id = s.brand_id WHERE s.id = ?",
            this::mapObj, id);
    }

    Series map(ResultSet rs, int rn) throws SQLException {
        Series obj = new Series();
        obj.setId(rs.getLong(1));
        obj.setBrandId(rs.getLong(2));
        obj.setName(rs.getString(3));
        return obj;
    }

    Series mapObj(ResultSet rs, int rn) throws SQLException {
        Brand jobj = new Brand();
        jobj.setId(rs.getLong(3));
        jobj.setName(rs.getString(4));

        Series obj = new Series();
        obj.setId(rs.getLong(1));
        obj.setName(rs.getString(2));
        obj.setBrand(jobj);
        return obj;
    }
}
