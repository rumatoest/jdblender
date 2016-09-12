package dbshaker.eclipselink;

import dbshaker.core.DbConnection;
import dbshaker.core.FrameworkRunner;
import java.util.HashMap;
import java.util.Map;
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

        emf = Persistence.createEntityManagerFactory("shaker", properties);
    }

    @Override
    public void createBrand(long id, String name) {
        throw new UnsupportedOperationException("Not supported yet.");
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
