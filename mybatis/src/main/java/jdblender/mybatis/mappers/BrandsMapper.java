package jdblender.mybatis.mappers;

import jdblender.mybatis.model.Brand;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;

public interface BrandsMapper {

    @Select("SELECT * FROM brands WHERE id = #{id}")
    public Brand get(long id);

    @Insert("INSERT INTO brands (id,name) VALUES (#{id}, #{name})")
    public void create(Brand brand);
}
