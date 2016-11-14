package jdblender.sdata.dao;

import jdblender.sdata.model.Series;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SeriesDao extends CrudRepository<Series, Long> {
    
}
