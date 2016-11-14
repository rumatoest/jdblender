package jdblender.jooq.model;

import java.util.Collections;
import java.util.List;
import jdblender.core.domain.SeriesObj;
import jdblender.core.domain.SpareObj;
import jdblender.jooq.db.public_.tables.Models;
import jdblender.jooq.db.public_.tables.records.ModelsRecord;
import org.jooq.Record;

public class ModelWrapper implements jdblender.core.domain.Model, jdblender.core.domain.ModelObj {

    Record record;

    ModelsRecord model;

    public ModelWrapper(ModelsRecord model) {
        this.model = model;
    }

    public ModelWrapper(Record record) {
        this.record = record;
    }

    @Override
    public long getId() {
        if (record != null) {
            return record.get(Models.MODELS.ID);
        }
        return model.getId();
    }

    @Override
    public long getSeriesId() {
        if (record != null) {
            return record.get(Models.MODELS.SERIES_ID);
        }
        return model.getSeriesId();
    }

    @Override
    public String getName() {
        if (record != null) {
            return record.get(Models.MODELS.NAME);
        }

        return model.getName();
    }

    @Override
    public SeriesObj getSeries() {
        return new SeriesWrapper(record);
    }

    List<? extends SpareObj> spares = Collections.EMPTY_LIST;

    public void setSpares(List<? extends SpareObj> spares) {
        this.spares = spares;
    }

    @Override
    public List<? extends SpareObj> getSpares() {
        return spares;
    }
}
