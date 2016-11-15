package jdblender.sjdbc.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import jdblender.sjdbc.model.Brand;
import jdblender.sjdbc.model.Model;
import jdblender.sjdbc.model.Series;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class ModelsDao extends CommonDao {

    @Autowired
    SparesDao sparesDao;

    public void create(long id, long seriesId, String name) {
        template().update("INSERT INTO models (id, series_id, name) VALUES (?,?,?)", id, seriesId, name);
    }

    public Model get(long id) {
        return template().queryForObject("SELECT id, series_id, name FROM models WHERE id = ?",
            this::map,
            id
        );
    }

    public Model getObj(long id) {
        return template().queryForObject("SELECT m.id, m.name, s.id s_id, s.name s_name, "
            + "b.id b_id, b.name b_name FROM models m "
            + "INNER JOIN series s ON s.id = m.series_id "
            + "INNER JOIN brands b ON b.id = s.brand_id "
            + "WHERE m.id = ?",
            this::mapObj,
            id
        );
    }

    public Model getWithSpares(long id) {
        Model obj = getObj(id);
        obj.setSpares(sparesDao.findByModel(id));
        return obj;
    }

    public void link2Spare(long modelId, long spareId) {
        template().update("INSERT INTO spare_to_model (spare_id, model_id) VALUES (?,?)", spareId, modelId);
    }

    Model map(ResultSet rs, int rn) throws SQLException {
        Model obj = new Model();
        obj.setId(rs.getLong(1));
        obj.setSeriesId(rs.getLong(2));
        obj.setName(rs.getString(3));
        return obj;
    }

    Model mapObj(ResultSet rs, int rn) throws SQLException {
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
        return obj;
    }

}
