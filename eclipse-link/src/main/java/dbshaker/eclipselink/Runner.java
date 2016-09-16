package dbshaker.eclipselink;

import dbshaker.core.DbConnection;
import dbshaker.core.FrameworkRunner;
import dbshaker.eclipselink.model.Brand;

import java.util.HashMap;
import java.util.Map;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 *
 */
public class Runner implements FrameworkRunner {

    EntityManagerFactory emf;

    @Override
    public void init(DbConnection connection) {
        Map<String, String> properties = new HashMap<String, String>();
        properties.put("javax.persistence.jdbc.driver", connection.driverClass);
        properties.put("javax.persistence.jdbc.url", connection.uri);
        properties.put("javax.persistence.jdbc.user", connection.username);
        properties.put("javax.persistence.jdbc.password", connection.password);
//        properties.put("eclipselink.logging.level", "ALL");
        emf = Persistence.createEntityManagerFactory("shaker", properties);
    }

    @Override
    public void close() {
        emf.close();
    }

    @Override
    public void createBrand(long id, String name) {
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        em.persist(new Brand(id, name));
        em.getTransaction().commit();
        em.close();
    }

    public Brand getBrand(long id) {
        EntityManager em = emf.createEntityManager();
        Brand b = em.find(Brand.class, id);
        em.close();
        return b;
    }
    
    @Override
    public void createModel(long id, long brandId, String name) {
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
