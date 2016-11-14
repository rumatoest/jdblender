package jdblender.jooq.model;

import jdblender.core.domain.Brand;
import jdblender.jooq.db.public_.tables.Spares;
import jdblender.jooq.db.public_.tables.records.SparesRecord;
import org.jooq.Record;

public class SpareWrapper implements jdblender.core.domain.Spare, jdblender.core.domain.SpareObj {

    Record rec;

    SparesRecord spare;

    public SpareWrapper(Record rec) {
        this.rec = rec;
    }

    public SpareWrapper(SparesRecord spare) {
        this.spare = spare;
    }

    @Override
    public long getId() {
        if (rec != null) {
            return rec.get(Spares.SPARES.ID);
        }
        return spare.getId();
    }

    @Override
    public long getBrandId() {
        if (rec != null) {
            return rec.get(Spares.SPARES.BRAND_ID);
        }
        return spare.getBrandId();
    }

    @Override
    public String getName() {
        if (rec != null) {
            return rec.get(Spares.SPARES.NAME);
        }
        return spare.getName();
    }

    @Override
    public String getLabel() {
        if (rec != null) {
            return rec.get(Spares.SPARES.LABEL);
        }
        return spare.getLabel();
    }

    @Override
    public boolean getFlag() {
        if (rec != null) {
            return rec.get(Spares.SPARES.FLAG);
        }

        return spare.getFlag();
    }

    @Override
    public int getNum() {
        if (rec != null) {
            return rec.get(Spares.SPARES.NUM);
        }

        return spare.getNum();
    }

    @Override
    public Brand getBrand() {
        return new BrandWrapper(rec);
    }
}
