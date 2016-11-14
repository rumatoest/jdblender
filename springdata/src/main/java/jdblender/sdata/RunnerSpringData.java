package jdblender.sdata;

import jdblender.core.DbConnection;
import jdblender.core.FrameworkRunner;
import jdblender.core.domain.Brand;
import jdblender.core.domain.Model;
import jdblender.core.domain.ModelObj;
import jdblender.core.domain.Series;
import jdblender.core.domain.SeriesObj;
import jdblender.core.domain.Spare;
import jdblender.core.domain.SpareObj;

import org.h2.jdbcx.JdbcConnectionPool;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.util.Collection;

public class RunnerSpringData implements FrameworkRunner {

    FrameworkRunner runner;

    @Override
    public int getFactor() {
        return 50;
    }

    @Override
    public void init(DbConnection connection) throws Exception {
        JdbcConnectionPool pool = JdbcConnectionPool.create(connection.uri, connection.username, connection.password);
        H2DS.init(pool);

        AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext(Config.class);
        runner = ctx.getBean(FrameworkRunner.class);
    }

    @Override
    public void close() throws Exception {
    }

    @Override
    public void createBrand(long id, String name) throws Exception {
        runner.createBrand(id, name);
    }

    @Override
    public Brand getBrand(long id) throws Exception {
        return runner.getBrand(id);
    }

    @Override
    public void createSeries(long id, long brandId, String name) throws Exception {
        runner.createSeries(id, brandId, name);
    }

    @Override
    public Series getSeries(long id) throws Exception {
        return runner.getSeries(id);
    }

    @Override
    public SeriesObj getSeriesObj(long id) throws Exception {
        return runner.getSeriesObj(id);
    }

    @Override
    public void createModel(long id, long seriesId, String name) throws Exception {
        runner.createModel(id, seriesId, name);
    }

    @Override
    public Model getModel(long id) throws Exception {
        return runner.getModel(id);
    }

    @Override
    public ModelObj getModelObj(long id) throws Exception {
        return runner.getModelObj(id);
    }

    @Override
    public ModelObj getModelObjWithSpares(long id) throws Exception {
        return runner.getModelObjWithSpares(id);
    }

    @Override
    public void createSpare(long id, long brandId, String name, String label, boolean flag, int num) throws Exception {
        runner.createSpare(id, brandId, name, label, flag, num);
    }

    @Override
    public Spare getSpare(long id) throws Exception {
        return runner.getSpare(id);
    }

    @Override
    public Collection<? extends Spare> getSpares(String label, Boolean flag, Integer numFromInclusive, Integer numToInclusive) throws Exception {
        return runner.getSpares(label, flag, numFromInclusive, numToInclusive);
    }

    @Override
    public SpareObj getSpareObj(long id) throws Exception {
        return runner.getSpareObj(id);
    }

    @Override
    public void linkModel2Spare(long modelId, long spareId) throws Exception {
        runner.linkModel2Spare(modelId, spareId);
    }

    @Override
    public void linkModel2SpareOptimized(long modelId, long spareId) throws Exception {
        runner.linkModel2SpareOptimized(modelId, spareId);
    }
}
