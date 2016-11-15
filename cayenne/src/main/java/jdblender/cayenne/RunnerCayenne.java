package jdblender.cayenne;

import java.util.ArrayList;
import java.util.Arrays;
import jdblender.cayenne.model.Brands;
import jdblender.cayenne.model.Models;
import jdblender.cayenne.model.Series;
import jdblender.cayenne.model.Spares;
import jdblender.core.DbConnection;
import jdblender.core.FrameworkRunner;
import jdblender.core.domain.*;
import org.apache.cayenne.ObjectContext;
import org.apache.cayenne.configuration.server.ServerRuntime;
import org.apache.cayenne.query.ObjectSelect;
import java.util.Collection;
import java.util.List;
import jdblender.cayenne.model.SpareToModel;
import org.apache.cayenne.exp.Expression;
import org.apache.cayenne.exp.ExpressionFactory;
import org.apache.cayenne.query.EJBQLQuery;
import org.apache.cayenne.query.SQLTemplate;
import org.apache.cayenne.query.SelectQuery;

/**
 *
 */
public class RunnerCayenne implements FrameworkRunner {

    private ObjectContext readContext;

    private ObjectContext writeContext;

    private ServerRuntime cayenneRuntime;

    @Override
    public int getFactor() {
        return 1;
    }

    @Override
    public void init(DbConnection connection) {
        cayenneRuntime = new ServerRuntime("cayenne-project.xml");
        readContext = cayenneRuntime.newContext();
        writeContext = readContext;
    }

    @Override
    public void close() {
        cayenneRuntime.shutdown();
    }

    @Override
    public void createBrand(long id, String name) {
        Brands brands = writeContext.newObject(Brands.class);
        brands.setId(id);
        brands.setName(name);
        writeContext.commitChanges();
    }

    public Brand getBrand(long id) {
        return ObjectSelect
            .query(Brands.class)
            .where(Brands.ID.eq(id))
            .selectOne(readContext);
    }

    @Override
    public void createSeries(long id, long brandId, String name) throws Exception {
        Series series = writeContext.newObject(Series.class);
        series.setId(id);
        series.setName(name);
        series.setBrandId(brandId);
        series.setBrand(ObjectSelect
            .query(Brands.class)
            .where(Brands.ID.eq(brandId))
            .selectOne(readContext));
        writeContext.commitChanges();
    }

    @Override
    public jdblender.core.domain.Series getSeries(long id) throws Exception {
        return ObjectSelect
            .query(Series.class)
            .where(Series.ID.eq(id))
            .selectOne(readContext);
    }

    @Override
    public SeriesObj getSeriesObj(long id) throws Exception {
        return ObjectSelect
            .query(Series.class)
            .prefetch(Series.BRAND.joint())
            .where(Series.ID.eq(id))
            .selectOne(readContext);
    }

    @Override
    public void createModel(long id, long seriesId, String name) {
        Models models = writeContext.newObject(Models.class);
        models.setId(id);
        models.setSeriesId(seriesId);
        models.setName(name);
        writeContext.commitChanges();
    }

    @Override
    public Model getModel(long id) {
        return ObjectSelect
            .query(Models.class)
            .where(Models.ID.eq(id))
            .selectOne(readContext);
    }

    @Override
    public ModelObj getModelObj(long id) {
        return ObjectSelect
            .query(Models.class)
            .prefetch(Models.SERIES.joint())
            .where(Models.ID.eq(id))
            .selectOne(readContext);
    }

    @Override
    public void createSpare(long id, long brandId, String name, String label, boolean flag, int num) {
        Spares spares = writeContext.newObject(Spares.class);
        spares.setId(id);
        spares.setName(name);
        spares.setLabel(label);
        spares.setFlag(flag);
        spares.setNum(num);
        spares.setBrandId(brandId);
        spares.setBrand(ObjectSelect
            .query(Brands.class)
            .where(Brands.ID.eq(brandId))
            .selectOne(readContext));
        writeContext.commitChanges();
    }

    @Override
    public Spare getSpare(long id) throws Exception {
        return ObjectSelect
            .query(Spares.class)
            .where(Spares.ID.eq(id))
            .selectOne(readContext);
    }

    @Override
    public SpareObj getSpareObj(long id) throws Exception {
        return ObjectSelect
            .query(Spares.class)
            .prefetch(Spares.BRAND.joint())
            .where(Spares.ID.eq(id))
            .selectOne(readContext);
    }

    @Override
    public void linkModel2Spare(long modelId, long spareId) {
        SpareToModel s2m = writeContext.newObject(SpareToModel.class);
        s2m.setModelId(modelId);
        s2m.setSpareId(spareId);
        writeContext.commitChanges();
    }

    @Override
    public void linkModel2SpareOptimized(long modelId, long spareId) throws Exception {
        // FIXME have no Idea how call native query here
        SpareToModel s2m = writeContext.newObject(SpareToModel.class);
        s2m.setModelId(modelId);
        s2m.setSpareId(spareId);
        writeContext.commitChanges();
    }

    @Override
    public ModelObj getModelObjWithSpares(long id) throws Exception {
        Models model = ObjectSelect
            .query(Models.class)
            .prefetch(Models.SERIES.joint())
            .where(Models.ID.eq(id))
            .selectOne(readContext);

        // This implementation is also sucks but I do not know how to do it in other way
        ArrayList<Long> sIds = new ArrayList<>();
        ObjectSelect
            .query(SpareToModel.class)
            .where(SpareToModel.MODEL_ID.eq(id))
            .select(readContext)
            .stream().forEach(it -> sIds.add(it.getSpareId()));

        List<Spares> spares = ObjectSelect
            .query(Spares.class)
            .prefetch(Spares.BRAND.joint())
            .where(Spares.ID.in(sIds))
            .select(readContext);

        model.setSpares(spares);
        return model;
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

        return (Collection)query.select(readContext);
    }
}
