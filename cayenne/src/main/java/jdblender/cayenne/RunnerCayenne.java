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
import org.apache.cayenne.query.ObjectSelect;

import java.util.Collection;

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
        series.setBrandId(brandId);
        series.setName(name);
        series.setId(id);
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
        // TODO
    }

    @Override
    public void linkModel2SpareOptimized(long modelId, long spareId) throws Exception {
        // TODO
    }

    @Override
    public ModelObj getModelObjWithSpares(long id) throws Exception {
        return null; // TODO
    }

    @Override
    public Collection<Spare> getSpares(String label, Boolean flag, Integer numFromInclusive, Integer numToInclusive) throws Exception {
        return (Collection) ObjectSelect
                .query(Spares.class)
                .where(Spares.FLAG.eq(flag))
                .and(Spares.LABEL.eq(label))
                .and(Spares.NUM.between(numFromInclusive, numToInclusive))
                .select(readContext);
    }
}
