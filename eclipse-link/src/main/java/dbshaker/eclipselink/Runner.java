package dbshaker.eclipselink;

import dbshaker.core.DbConnection;
import dbshaker.core.FrameworkRunner;
import dbshaker.eclipselink.dao.BrandsDao;
import dbshaker.eclipselink.dao.ModelsDao;
import dbshaker.eclipselink.dao.SeriesDao;
import dbshaker.eclipselink.model.Brand;
import dbshaker.eclipselink.model.Series;

import java.util.HashMap;
import java.util.Map;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 *
 */
public class Runner implements FrameworkRunner {

    private EntityManagerFactory emf;

    private BrandsDao brandsDao;

    private SeriesDao seriesDao;

    private ModelsDao modelsDao;

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
    }

    @Override
    public void close() {
        emf.close();
    }

    @Override
    public void createBrand(long id, String name) {
        brandsDao.persist(new Brand(id, name));
    }

    @Override
    public void createSeries(long id, long brandId, String name) throws Exception {
        Brand brand = brandsDao.getByPk(brandId);
        seriesDao.persist(new Series(id, brand, name));
    }

    public Brand getBrand(long id) {
        return brandsDao.getByPk(id);
    }

    @Override
    public void createModel(long id, long brandId, String name) {
        Brand Brand = brandsDao.getByPk(brandId);

        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void createModelVariant(long id, long modelId, String name) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void createSpare(long id, String name) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void linkSpare2ModelVariant(long spareId, long modelVariantId) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

}
