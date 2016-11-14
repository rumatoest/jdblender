package jdblender.sdata.dao;

import jdblender.sdata.model.Spare;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface SparesDao extends CrudRepository<Spare, Long> {

    @Modifying
    @Transactional
    @Query(value = "INSERT INTO spare_to_model (model_id, spare_id) VALUES (?1,?2)", nativeQuery = true)
    void linkSpare2Models(long modelId, long spareId);

    List<Spare> findByLabelAndFlagAndNum(String label, Boolean flag, Integer num);
}
