package dbshaker.eclipselink.dao;

import dbshaker.eclipselink.model.Model;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

public class ModelsDao {

    private final EntityManagerFactory emf;

    public ModelsDao(EntityManagerFactory emf) {
        this.emf = emf;
    }

    public void persist(Model model) {
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        em.persist(model);
        em.getTransaction().commit();
    }

    public Model getByPk(long id) {
        return emf.createEntityManager().find(Model.class, id);
    }

    public Model getByPkObj(long id) {
        Model entity = emf.createEntityManager().find(Model.class, id);
        entity.getSeries().getBrand();
        return entity;
    }
}
