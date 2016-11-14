package jdblender.sdata.dao;

import jdblender.sdata.model.Model;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ModelsDao extends CrudRepository<Model, Long> {

}
