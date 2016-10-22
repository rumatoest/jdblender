package dbshaker.eclipselink.dao;

import dbshaker.eclipselink.model.Spare;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author rumatoest
 */
public class SparesDao {

    private final EntityManagerFactory emf;

    public SparesDao(EntityManagerFactory emf) {
        this.emf = emf;
    }

    public void persist(Spare spare) {
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        em.persist(spare);
        em.getTransaction().commit();
    }

    public Spare getByPk(long id) {
        return emf.createEntityManager().find(Spare.class, id);
    }

    public Spare getByPkObj(long id) {
        Spare entity = emf.createEntityManager().find(Spare.class, id);
        entity.getBrand().getName();
        return entity;
    }
}
