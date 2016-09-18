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
}
