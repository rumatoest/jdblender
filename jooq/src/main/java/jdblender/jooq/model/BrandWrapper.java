package jdblender.jooq.model;

import jdblender.jooq.db.public_.tables.Brands;
import jdblender.jooq.db.public_.tables.records.BrandsRecord;

import org.jooq.Record;

public class BrandWrapper implements jdblender.core.domain.Brand {

    BrandsRecord result;

    Record record;

    public BrandWrapper(BrandsRecord result) {
        this.result = result;
    }

    public BrandWrapper(Record record) {
        this.record = record;
    }

    @Override
    public long getId() {
        if (record == null) {
            return result.getId();
        }

        return record.getValue(Brands.BRANDS.ID);
    }

    @Override
    public String getName() {
        if (record == null) {
            return result.getName();
        }

        return record.getValue(Brands.BRANDS.NAME);
    }
}
