package jdblender.sdata.dao;

import jdblender.sdata.model.Brand;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BrandsDao extends CrudRepository<Brand, Long> {

}
