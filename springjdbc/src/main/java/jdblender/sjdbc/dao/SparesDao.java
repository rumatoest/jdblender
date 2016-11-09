package jdblender.sjdbc.dao;

import jdblender.sjdbc.model.Brand;
import jdblender.sjdbc.model.Spare;

import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import javax.annotation.PostConstruct;

@Repository
public class SparesDao extends CommonDao {

    NamedParameterJdbcTemplate templateNamed;

    @PostConstruct
    public void postConstruct() {
        templateNamed = new NamedParameterJdbcTemplate(ds);
    }

    public void create(long id, long brandId, String name, String label, boolean flag, int num) {
        template().update("INSERT INTO spares (id, brand_id, name, label, flag, num) VALUES (?,?,?,?,?,?)",
            id, brandId, name, label, flag, num);
    }

    public Spare get(long id) {
        return template().queryForObject("SELECT s.id, s.name, s.label, s.flag, s.num, s.brand_id "
            + "FROM spares s WHERE s.id = ?",
            this::resultToSpare, id);
    }

    public Spare getObj(long id) {
        return template().queryForObject("SELECT s.id, s.name, s.label, s.flag, s.num, s.brand_id, "
            + "b.name brand_name FROM spares s "
            + "INNER JOIN brands b ON b.id = s.brand_id WHERE s.id = ?",
            this::resultToSpareObj,
            id);
    }

    public Collection<Spare> find(String label, Boolean flag, Integer numFromInclusive, Integer numToInclusive) {

        HashMap<String, Object> params = new HashMap<>();
        StringBuilder sb = new StringBuilder("SELECT s.id, s.name, s.label, s.flag, s.num, s.brand_id "
            + "FROM spares s WHERE ");

        if (label != null) {
            sb.append(" s.label = :label ");
            params.put("label", label);
        }

        if (flag != null) {
            if (!params.isEmpty()) {
                sb.append(" AND ");
            }
            sb.append(" s.flag = :flag ");
            params.put("flag", flag);
        }

        if (numFromInclusive != null) {
            if (!params.isEmpty()) {
                sb.append(" AND ");
            }

            if (numToInclusive == null) {
                sb.append("s.num = :num");
                params.put("num", numFromInclusive);
            } else {
                sb.append("s.num >= :nfrom AND s.num <= :nto");
                params.put("nfrom", numFromInclusive);
                params.put("nto", numToInclusive);
            }
        }

        return templateNamed.query(sb.toString(), params, this::resultToSpare);
    }

    List<Spare> findByModel(long id) {
        return template().query("SELECT s.id, s.name, s.label, s.flag, s.num, "
            + "s.brand_id, b.name brand_name FROM spares s "
            + "INNER JOIN brands b ON b.id = s.brand_id "
            + "INNER JOIN spare_to_model m2m ON m2m.spare_id = s.id "
            + "WHERE m2m.model_id = ?",
            this::resultToSpareObj,
            id);
    }

    Spare resultToSpare(ResultSet rs, int rn) throws SQLException {
        Spare spare = new Spare();
        spare.setId(rs.getLong(1));
        spare.setName(rs.getString(2));
        spare.setLabel(rs.getString(3));
        spare.setFlag(rs.getBoolean(4));
        spare.setNum(rs.getInt(5));
        spare.setBrandId(rs.getLong(6));
        return spare;
    }

    Spare resultToSpareObj(ResultSet rs, int rn) throws SQLException {
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

}
