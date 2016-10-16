package dbshaker.hibernate;

import dbshaker.core.DbConnection;
import dbshaker.core.FrameworkRunner;
import dbshaker.hibernate.dao.BrandsDao;
import dbshaker.hibernate.dao.ModelsDao;
import dbshaker.hibernate.dao.SeriesDao;
import dbshaker.hibernate.model.Brand;
import dbshaker.hibernate.model.Model;
import dbshaker.hibernate.model.Series;

import org.hibernate.SessionFactory;
import org.hibernate.boot.Metadata;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;

/**
 *
 */
public class RunnerHibernate implements FrameworkRunner {

    private SessionFactory sf;

    private BrandsDao brandsDao;

    private SeriesDao seriesDao;

    private ModelsDao modelsDao;

    @Override
    public void init(DbConnection connection) {
        Configuration cfg = new Configuration();
        cfg.setProperty("hibernate.connection.url", connection.uri);
        cfg.setProperty("hibernate.connection.username", connection.username);
        cfg.setProperty("hibernate.connection.password", connection.password);
        //cfg.setProperty("hibernate.show_sql", "true");
        cfg.configure("hibernate.cfg.xml");

        SessionFactory sf = cfg.buildSessionFactory();

        brandsDao = new BrandsDao(sf);
        seriesDao = new SeriesDao(sf);
        modelsDao = new ModelsDao(sf);
    }

    @Override
    public void close() {
        if (sf != null && !sf.isClosed()) {
            sf.close();
        }
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
    public void createSpare(long id, String name) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void linkSpare2ModelVariant(long spareId, long modelVariantId) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
