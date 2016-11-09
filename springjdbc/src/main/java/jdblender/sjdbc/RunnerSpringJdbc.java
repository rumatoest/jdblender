package jdblender.sjdbc;

import jdblender.core.DbConnection;
import jdblender.core.FrameworkRunner;
import jdblender.sjdbc.dao.BrandsDao;
import jdblender.sjdbc.dao.ModelsDao;
import jdblender.sjdbc.dao.SeriesDao;
import jdblender.sjdbc.dao.SparesDao;
import jdblender.sjdbc.model.Brand;
import jdblender.sjdbc.model.Model;
import jdblender.sjdbc.model.Series;
import jdblender.sjdbc.model.Spare;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import org.h2.jdbcx.JdbcDataSource;

@Service
public class RunnerSpringJdbc implements FrameworkRunner {

    @Autowired
    JdbcDataSource h2ds;

    @Autowired
    BrandsDao brandsDao;

    @Autowired
    ModelsDao modelsDao;

    @Autowired
    SeriesDao seriesDao;

    @Autowired
    SparesDao sparesDao;

    @Override
    public void init(DbConnection connection) throws Exception {
        h2ds.setURL(connection.uri);
        h2ds.setUser(connection.username);
        h2ds.setPassword(connection.password);
    }

    @Override
    public void close() throws Exception {
    }

    @Override
    public void createBrand(long id, String name) throws Exception {
        brandsDao.create(id, name);
    }

    @Override
    public Brand getBrand(long id) throws Exception {
        return brandsDao.get(id);
    }

    @Override
    public void createSeries(long id, long brandId, String name) throws Exception {
        seriesDao.create(id, brandId, name);
    }

    @Override
    public Series getSeries(long id) throws Exception {
        return seriesDao.get(id);
    }

    @Override
    public Series getSeriesObj(long id) throws Exception {
        return seriesDao.getObj(id);
    }

    @Override
    public void createModel(long id, long seriesId, String name) throws Exception {
        modelsDao.create(id, seriesId, name);
    }

    @Override
    public Model getModel(long id) throws Exception {
        return modelsDao.get(id);
    }

    @Override
    public Model getModelObj(long id) throws Exception {
        return modelsDao.getObj(id);
    }

    @Override
    public Model getModelObjWithSpares(long id) throws Exception {
        return modelsDao.getWithSpares(id);
    }

    @Override
    public void createSpare(long id, long brandId, String name, String label, boolean flag, int num) throws Exception {
        sparesDao.create(id, brandId, name, label, flag, num);
    }

    @Override
    public Spare getSpare(long id) throws Exception {
        return sparesDao.get(id);
    }

    @Override
    public Collection<Spare> getSpares(String label, Boolean flag, Integer numFromInclusive, Integer numToInclusive) throws Exception {
        return sparesDao.find(label, flag, numFromInclusive, numToInclusive);
    }

    @Override
    public Spare getSpareObj(long id) throws Exception {
        return sparesDao.getObj(id);
    }

    @Override
    public void linkModel2Spare(long modelId, long spareId) throws Exception {
        modelsDao.link2Spare(modelId, spareId);
    }

    @Override
    public void linkModel2SpareOptimized(long modelId, long spareId) throws Exception {
        modelsDao.link2Spare(modelId, spareId);
    }

}
