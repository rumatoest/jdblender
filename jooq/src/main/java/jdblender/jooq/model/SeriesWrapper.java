package jdblender.jooq.model;

import jdblender.core.domain.Brand;
import jdblender.core.domain.SeriesObj;
import jdblender.jooq.db.public_.tables.Series;
import jdblender.jooq.db.public_.tables.records.SeriesRecord;

import org.jooq.Record;

public class SeriesWrapper implements jdblender.core.domain.Series, SeriesObj {

    SeriesRecord series;

    Record rec;

    public SeriesWrapper(SeriesRecord series) {
        this.series = series;
    }

    public SeriesWrapper(Record record) {
        this.rec = record;
    }

    @Override
    public long getId() {
        if (rec == null) {
            return series.getId();
        } else {
            return rec.getValue(Series.SERIES.ID);
        }
    }

    @Override
    public long getBrandId() {
        if (rec == null) {
            return series.getBrandId();
        }
        return rec.getValue(Series.SERIES.BRAND_ID);
    }

    @Override
    public String getName() {
        if (rec == null) {
            return series.getName();
        }
        return rec.getValue(Series.SERIES.NAME);
    }

    @Override
    public Brand getBrand() {
        return new BrandWrapper(rec);
    }

}
