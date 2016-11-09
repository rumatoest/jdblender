package jdblender.mybatis.mappers;

import jdblender.mybatis.model.Series;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.ResultMap;
import org.apache.ibatis.annotations.Select;

public interface SeriesMapper {

    @Select("SELECT * FROM series where id = #{id}")
    public Series get(long id);

    @Select("SELECT s.id, s.name, s.brand_id, b.name brand_name, b.id brand_id FROM series s "
        + "INNER JOIN brands b ON b.id = s.brand_id WHERE s.id = #{id}")
    @ResultMap("seriesObj")
    public Series getObj(long id);

    @Insert("INSERT INTO series (id, brand_id, name) VALUES (#{id},#{brandId},#{name})")
    public void create(Series series);

}
