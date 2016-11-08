package jdblender.eclipselink.dao;

import jdblender.eclipselink.model.Brand;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

public class BrandsDao {

    private final EntityManagerFactory emf;

    public BrandsDao(EntityManagerFactory emf) {
        this.emf = emf;
    }

    public Brand getByPk(long id) {
        return emf.createEntityManager().find(Brand.class, id);
    }

    public void persist(Brand brand) {
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        em.persist(brand);
        em.getTransaction().commit();
    }

}
