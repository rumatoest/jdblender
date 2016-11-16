package jdblender.cayenne;

import jdblender.cayenne.model.Brands;
import jdblender.cayenne.model.Models;
import jdblender.cayenne.model.Series;
import jdblender.cayenne.model.Spares;
import jdblender.core.DbConnection;
import jdblender.core.FrameworkRunner;
import jdblender.core.domain.*;

import org.apache.cayenne.ObjectContext;
import org.apache.cayenne.configuration.server.ServerRuntime;
import org.apache.cayenne.configuration.server.ServerRuntimeBuilder;
import org.apache.cayenne.query.*;
import org.h2.jdbcx.JdbcConnectionPool;

import java.util.Collection;

public class RunnerCayenne implements FrameworkRunner {

    private ObjectContext context;

    private ServerRuntime cayenneRuntime;

    @Override
    public int getFactor() {
        return 1;
    }

    @Override
    public void init(DbConnection connection) {
        cayenneRuntime = ServerRuntimeBuilder.builder()
            .addModule(new CayenneModule())
            .addConfig("cayenne-project.xml")
            .dataSource(JdbcConnectionPool.create(connection.uri, connection.username, connection.password))
            .build();
        context = cayenneRuntime.newContext();
    }

    @Override
    public void close() {
        cayenneRuntime.shutdown();
    }

    @Override
    public void createBrand(long id, String name) {
        Brands brands = context.newObject(Brands.class);
        brands.setId(id);
        brands.setName(name);
        context.commitChanges();
    }

    @Override
    public Brand getBrand(long id) {
        return SelectById
            .query(Brands.class, id)
            .selectOne(context);
    }

    @Override
    public void createSeries(long id, long brandId, String name) throws Exception {
        Series series = context.newObject(Series.class);
        series.setId(id);
        series.setName(name);
        series.setBrand(SelectById
            .query(Brands.class, brandId)
            .selectOne(context));
        context.commitChanges();
    }

    @Override
    public jdblender.core.domain.Series getSeries(long id) throws Exception {
        return SelectById
            .query(Series.class, id)
            .selectOne(context);
    }

    @Override
    public SeriesObj getSeriesObj(long id) throws Exception {
        return SelectById
            .query(Series.class, id)
            .prefetch(Series.BRAND.joint())
            .selectOne(context);
    }

    @Override
    public void createModel(long id, long seriesId, String name) {
        Models models = context.newObject(Models.class);
        models.setId(id);
        models.setSeries(SelectById
            .query(Series.class, seriesId)
            .selectOne(context));
        models.setName(name);
        context.commitChanges();
    }

    @Override
    public Model getModel(long id) {
        return SelectById
            .query(Models.class, id)
            .selectOne(context);
    }

    @Override
    public ModelObj getModelObj(long id) {
        return SelectById
            .query(Models.class, id)
            .prefetch(Models.SERIES.joint())
            .selectOne(context);
    }

    @Override
    public void createSpare(long id, long brandId, String name, String label, boolean flag, int num) {
        Spares spares = context.newObject(Spares.class);
        spares.setId(id);
        spares.setName(name);
        spares.setLabel(label);
        spares.setFlag(flag);
        spares.setNum(num);
        spares.setBrand(SelectById
            .query(Brands.class, brandId)
            .selectOne(context));
        context.commitChanges();
    }

    @Override
    public Spare getSpare(long id) throws Exception {
        return SelectById
            .query(Spares.class, id)
            .selectOne(context);
    }

    @Override
    public SpareObj getSpareObj(long id) throws Exception {
        return SelectById
            .query(Spares.class, id)
            .prefetch(Spares.BRAND.joint())
            .selectOne(context);
    }

    @Override
    public void linkModel2Spare(long modelId, long spareId) {
        Models models = SelectById
            .query(Models.class, modelId)
            .selectOne(context);

        Spares spares = SelectById
            .query(Spares.class, spareId)
            .selectOne(context);

        spares.addToModels(models);

        context.commitChanges();
    }

    @Override
    public void linkModel2SpareOptimized(long modelId, long spareId) throws Exception {
        SQLExec
            .query("INSERT INTO spare_to_model (spare_id, model_id) VALUES (#bind($spareId), #bind($modelId))")
            .paramsArray(spareId, modelId)
            .execute(context);
    }

    @Override
    public ModelObj getModelObjWithSpares(long id) throws Exception {
        return ObjectSelect
            .query(Models.class)
            .prefetch(Models.SERIES.joint())
            .prefetch(Models.SPARES.joint())
            .where(Models.ID.eq(id))
            .selectOne(context);
    }

    @Override
    public Collection<Spare> getSpares(String label, Boolean flag, Integer numFromInclusive, Integer numToInclusive) throws Exception {
        ObjectSelect<Spares> query = ObjectSelect.query(Spares.class);

        if (flag != null) {
            query = query.and(Spares.FLAG.eq(flag));
        }

        if (label != null) {
            query = query.and(Spares.LABEL.eq(label));
        }

        if (numFromInclusive != null) {
            if (numToInclusive != null) {
                query = query.and(Spares.NUM.between(numFromInclusive, numToInclusive));
            } else {
                query = query.and(Spares.NUM.eq(numFromInclusive));
            }
        }

        return (Collection)query.select(context);
    }
}
