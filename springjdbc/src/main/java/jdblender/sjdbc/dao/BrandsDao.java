package jdblender.sjdbc.dao;

import jdblender.sjdbc.model.Brand;

import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;

@Repository
public class BrandsDao extends CommonDao {

    public void create(long id, String name) {
        template().update("INSERT INTO brands (id,name) VALUES (?,?)", id, name);
    }

    public Brand get(long id) {
        return template().queryForObject("SELECT id, name FROM brands WHERE id = ?",
            this::map, id
        );
    }

    Brand map(ResultSet rs, int rn) throws SQLException {
        Brand brand = new Brand();
        brand.setId(rs.getLong(1));
        brand.setName(rs.getString(2));
        return brand;
    }
}
