package dbshaker.eclipselink.dao;

import dbshaker.eclipselink.model.Series;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

public class SeriesDao {

    private final EntityManagerFactory emf;

    public SeriesDao(EntityManagerFactory emf) {
        this.emf = emf;
    }

    public void persist(Series series) {
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        em.persist(series);
        em.getTransaction().commit();
    }

    public Series getByPk(long id) {
        return emf.createEntityManager().find(Series.class, id);
    }

    public Series getByPkObj(long id) {
        Series entity = emf.createEntityManager().find(Series.class, id);
        entity.getBrand();
        return entity;
    }
}
