package jdblender.jooq;

import jdblender.core.DbConnection;
import jdblender.core.FrameworkRunner;
import jdblender.jooq.db.public_.tables.Brands;
import jdblender.jooq.db.public_.tables.Models;
import jdblender.jooq.db.public_.tables.Series;
import jdblender.jooq.db.public_.tables.SpareToModel;
import jdblender.jooq.db.public_.tables.Spares;
import jdblender.jooq.db.public_.tables.records.BrandsRecord;
import jdblender.jooq.model.BrandWrapper;
import jdblender.jooq.model.ModelWrapper;
import jdblender.jooq.model.SeriesWrapper;
import jdblender.jooq.model.SpareWrapper;

import org.h2.jdbcx.JdbcConnectionPool;
import org.jooq.DSLContext;
import org.jooq.Record;
import org.jooq.SQLDialect;
import org.jooq.SelectQuery;
import org.jooq.impl.DSL;

import java.util.Collection;
import java.util.List;

public class RunnerJooq implements FrameworkRunner {

    DSLContext jooq;

    @Override
    public int getFactor() {
        return 1;
    }

    @Override
    public void init(DbConnection connection) throws Exception {
        JdbcConnectionPool pool = JdbcConnectionPool.create(connection.uri, connection.username, connection.password);

        jooq = DSL.using(pool, SQLDialect.H2);
    }

    @Override
    public void close() throws Exception {
    }

    @Override
    public void createBrand(long id, String name) throws Exception {
        jooq.insertInto(Brands.BRANDS)
            .set(Brands.BRANDS.ID, id)
            .set(Brands.BRANDS.NAME, name)
            .execute();
    }

    @Override
    public BrandWrapper getBrand(long id) throws Exception {
        BrandsRecord result = jooq.fetchOne(Brands.BRANDS, Brands.BRANDS.ID.eq(id));
        return new BrandWrapper(result);
    }

    @Override
    public void createSeries(long id, long brandId, String name) throws Exception {
        jooq.insertInto(Series.SERIES)
            .set(Series.SERIES.ID, id)
            .set(Series.SERIES.BRAND_ID, brandId)
            .set(Series.SERIES.NAME, name)
            .execute();
    }

    @Override
    public SeriesWrapper getSeries(long id) throws Exception {
        return new SeriesWrapper(jooq.fetchOne(Series.SERIES, Series.SERIES.ID.eq(id)));
    }

    @Override
    public SeriesWrapper getSeriesObj(long id) throws Exception {
        Record record = jooq.select().from(Series.SERIES)
            .join(Brands.BRANDS)
            .on(Brands.BRANDS.ID.eq(Series.SERIES.BRAND_ID))
            .where(Series.SERIES.ID.eq(id))
            .fetchOne();
        return new SeriesWrapper(record);
    }

    @Override
    public void createModel(long id, long seriesId, String name) throws Exception {
        jooq.insertInto(Models.MODELS)
            .set(Models.MODELS.ID, id)
            .set(Models.MODELS.SERIES_ID, seriesId)
            .set(Models.MODELS.NAME, name)
            .execute();
    }

    @Override
    public ModelWrapper getModel(long id) throws Exception {
        return new ModelWrapper(jooq.fetchOne(Models.MODELS, Models.MODELS.ID.eq(id)));
    }

    @Override
    public ModelWrapper getModelObj(long id) throws Exception {
        Record record = jooq.select().from(Models.MODELS)
            .join(Series.SERIES).on(Series.SERIES.ID.eq(Models.MODELS.SERIES_ID))
            .join(Brands.BRANDS).on(Brands.BRANDS.ID.eq(Series.SERIES.BRAND_ID))
            .where(Models.MODELS.ID.eq(id))
            .fetchOne();
        return new ModelWrapper(record);
    }

    @Override
    public ModelWrapper getModelObjWithSpares(long id) throws Exception {
        List<SpareWrapper> spares = jooq.select().from(Spares.SPARES)
            .join(Brands.BRANDS).on(Brands.BRANDS.ID.eq(Spares.SPARES.BRAND_ID))
            .join(SpareToModel.SPARE_TO_MODEL)
            .on(SpareToModel.SPARE_TO_MODEL.SPARE_ID.eq(Spares.SPARES.ID))
            .where(SpareToModel.SPARE_TO_MODEL.MODEL_ID.eq(id))
            .fetch(r -> new SpareWrapper(r));

        Record record = jooq.select().from(Models.MODELS)
            .join(Series.SERIES).on(Series.SERIES.ID.eq(Models.MODELS.SERIES_ID))
            .join(Brands.BRANDS).on(Brands.BRANDS.ID.eq(Series.SERIES.BRAND_ID))
            .where(Models.MODELS.ID.eq(id))
            .fetchOne();

        ModelWrapper model = new ModelWrapper(record);
        model.setSpares(spares);

        return model;
    }

    @Override
    public void createSpare(long id, long brandId, String name, String label, boolean flag, int num) throws Exception {
        jooq.insertInto(Spares.SPARES)
            .set(Spares.SPARES.ID, id)
            .set(Spares.SPARES.BRAND_ID, brandId)
            .set(Spares.SPARES.NAME, name)
            .set(Spares.SPARES.LABEL, label)
            .set(Spares.SPARES.FLAG, flag)
            .set(Spares.SPARES.NUM, num)
            .execute();
    }

    @Override
    public SpareWrapper getSpare(long id) throws Exception {
        return new SpareWrapper(jooq.fetchOne(Spares.SPARES, Spares.SPARES.ID.eq(id)));
    }

    @Override
    public SpareWrapper getSpareObj(long id) throws Exception {
        Record record = jooq.select().from(Spares.SPARES)
            .join(Brands.BRANDS).on(Brands.BRANDS.ID.eq(Spares.SPARES.BRAND_ID))
            .where(Spares.SPARES.ID.eq(id))
            .fetchOne();
        return new SpareWrapper(record);
    }

    @Override
    public Collection<SpareWrapper> getSpares(String label, Boolean flag, Integer numFromInclusive, Integer numToInclusive) throws Exception {
        SelectQuery<Record> query = jooq.selectQuery();
        query.addFrom(Spares.SPARES);

        if (label != null) {
            query.addConditions(Spares.SPARES.LABEL.eq(label));
        }

        if (flag != null) {
            query.addConditions(Spares.SPARES.FLAG.eq(flag));
        }

        if (numFromInclusive != null) {

            if (numToInclusive == null) {
                query.addConditions(Spares.SPARES.NUM.eq(numFromInclusive));
            } else {
                query.addConditions(Spares.SPARES.NUM.ge(numFromInclusive));
                query.addConditions(Spares.SPARES.NUM.le(numToInclusive));
            }
        }

        return query.fetch(r -> new SpareWrapper(r));
    }

    @Override
    public void linkModel2Spare(long modelId, long spareId) throws Exception {
        jooq.insertInto(SpareToModel.SPARE_TO_MODEL)
            .set(SpareToModel.SPARE_TO_MODEL.MODEL_ID, modelId)
            .set(SpareToModel.SPARE_TO_MODEL.SPARE_ID, spareId)
            .execute();
    }

    @Override
    public void linkModel2SpareOptimized(long modelId, long spareId) throws Exception {
        jooq.query("INSERT INTO spare_to_model (spare_id, model_id) VALUES (?,?)", spareId, modelId)
            .execute();
    }

}
