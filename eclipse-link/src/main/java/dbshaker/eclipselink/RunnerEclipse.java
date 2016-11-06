package dbshaker.eclipselink;

import dbshaker.core.DbConnection;
import dbshaker.core.FrameworkRunner;
import dbshaker.core.domain.ModelObj;
import dbshaker.eclipselink.dao.BrandsDao;
import dbshaker.eclipselink.dao.ModelsDao;
import dbshaker.eclipselink.dao.SeriesDao;
import dbshaker.eclipselink.dao.SparesDao;
import dbshaker.eclipselink.model.Brand;
import dbshaker.eclipselink.model.Model;
import dbshaker.eclipselink.model.Series;
import dbshaker.eclipselink.model.Spare;
import java.util.Collection;

import java.util.HashMap;
import java.util.Map;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 *
 */
public class RunnerEclipse implements FrameworkRunner {

    private EntityManagerFactory emf;

    private BrandsDao brandsDao;

    private SeriesDao seriesDao;

    private ModelsDao modelsDao;

    private SparesDao sparesDao;

    @Override
    public void init(DbConnection connection) {
        Map<String, String> properties = new HashMap<String, String>();
        properties.put("javax.persistence.jdbc.driver", connection.driverClass);
        properties.put("javax.persistence.jdbc.url", connection.uri);
        properties.put("javax.persistence.jdbc.user", connection.username);
        properties.put("javax.persistence.jdbc.password", connection.password);
        // properties.put("eclipselink.logging.level", "ALL");
        emf = Persistence.createEntityManagerFactory("shaker", properties);

        // Initialize DAOs
        brandsDao = new BrandsDao(emf);
        seriesDao = new SeriesDao(emf);
        modelsDao = new ModelsDao(emf);
        sparesDao = new SparesDao(emf);
    }

    @Override
    public void close() {
        emf.close();
    }

    @Override
    public void createBrand(long id, String name) {
        brandsDao.persist(new Brand(id, name));
    }

    public Brand getBrand(long id) {
        return brandsDao.getByPk(id);
    }

    @Override
    public void createSeries(long id, long brandId, String name) throws Exception {
        Brand brand = brandsDao.getByPk(brandId);
        seriesDao.persist(new Series(id, brand, name));
    }

    @Override
    public Series getSeries(long id) throws Exception {
        return seriesDao.getByPk(id);
    }

    @Override
    public Series getSeriesObj(long id) throws Exception {
        return seriesDao.getByPkObj(id);
    }

    @Override
    public void createModel(long id, long seriesId, String name) {
        Series series = seriesDao.getByPk(seriesId);
        modelsDao.persist(new Model(id, series, name));
    }

    @Override
    public Model getModel(long id) {
        return modelsDao.getByPk(id);
    }

    @Override
    public Model getModelObj(long id) {
        return modelsDao.getByPkObj(id);
    }

    @Override
    public void createSpare(long id, long brandId, String name, String label, boolean flag, int num) throws Exception {
        Spare spare = new Spare();
        spare.setId(id);
        spare.setName(name);
        spare.setLabel(label);
        spare.setFlag(flag);
        spare.setNum(num);
        spare.setBrand(brandsDao.getByPk(brandId));

        sparesDao.persist(spare);
    }

    @Override
    public Spare getSpare(long id) throws Exception {
        return sparesDao.getByPk(id);
    }

    @Override
    public Spare getSpareObj(long id) throws Exception {
        return sparesDao.getByPkObj(id);
    }

    @Override
    public void linkModel2Spare(long modelId, long spareId) {
        modelsDao.link2Spare(modelId, spareId);
    }

    @Override
    public void linkModel2SpareOptimized(long modelId, long spareId) throws Exception {
        modelsDao.link2SpareFast(modelId, spareId);
    }

    @Override
    public ModelObj getModelObjWithSpares(long id) throws Exception {
        return modelsDao.getByPkObjSpares(id);
    }

    @Override
    public Collection<Spare> getSpares(String label, Boolean flag, Integer numFromInclusive, Integer numToInclusive) throws Exception {
        return sparesDao.findSpares(label, flag, numFromInclusive, numToInclusive);
    }
}
