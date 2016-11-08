package jdblender.mybatis.mappers;

import jdblender.mybatis.dao.Spare;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;

import java.util.Collection;
import java.util.List;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.ResultMap;

public interface SparesMapper {

    @Select("SELECT s.* FROM spares s WHERE s.id = #{id}")
    public Spare get(long id);

    @Select("SELECT s.id, s.name, s.label, s.flag, s.num, s.brand_id, "
        + "b.id brand_id, b.name brand_name "
        + "FROM spares s INNER JOIN brands b ON b.id = s.brand_id WHERE s.id = #{id}")
    @ResultMap("spareObj")
    public Spare getObj(long id);

    public List<Spare> findSpares(
        @Param("label") String label,
        @Param("flag") Boolean flag,
        @Param("nFrom") Integer numFromInclusive,
        @Param("nTo") Integer numToInclusive
    );

    @Insert("INSERT INTO spares (id, brand_id, name, label, flag, num) VALUES (#{id},#{brandId},#{name},#{label},#{flag},#{num})")
    public void create(Spare spare);

}
