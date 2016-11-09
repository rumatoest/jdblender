package jdblender.mybatis.mappers;

import jdblender.mybatis.model.Model;
import jdblender.mybatis.model.Spare;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.ResultMap;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface ModelsMapper {

    @Select("SELECT * FROM models WHERE id = #{id}")
    public Model get(long id);

    @Select("SELECT m.id, m.name, m.series_id, "
        + "s.id series_id, s.name series_name, s.brand_id series_brand_id, "
        + "b.id brand_id, b.name brand_name "
        + "FROM models m "
        + "INNER JOIN series s ON s.id = m.series_id "
        + "INNER JOIN brands b ON b.id = s.brand_id "
        + "WHERE m.id = #{id}")
    @ResultMap("modelObj")
    public Model getObj(long id);

    public Model getWithSpares(long id);

    public List<Spare> findSparesForModel(long id);

    @Insert("INSERT INTO models (id, series_id, name) VALUES (#{id},#{seriesId},#{name})")
    public void create(Model model);

    @Insert("INSERT INTO spare_to_model (spare_id, model_id) VALUES (#{spareId},#{modelId})")
    public void link2Spare(@Param("modelId") long modelId, @Param("spareId") long spareId);

}
