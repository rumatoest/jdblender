package dbshaker.eclipselink.dao;

import dbshaker.eclipselink.model.Model;
import dbshaker.eclipselink.model.Spare;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;

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

    public Model getByPkObjSpares(long id) {
        Model entity = emf.createEntityManager().find(Model.class, id);
        entity.getSeries().getBrand();
        entity.getSpares().isEmpty();
        return entity;
    }

    public void link2Spare(long modelId, long spareId) {
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        Model model = em.find(Model.class, modelId);
        model.getSpares().add(em.find(Spare.class, spareId));
        em.persist(model);
        em.getTransaction().commit();
    }

    public void link2SpareFast(long modelId, long spareId) {
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        Query q = em.createNativeQuery("INSERT INTO spare_to_model (spare_id, model_id) VALUES (?, ?)");
        q.setParameter(1, spareId);
        q.setParameter(2, modelId);
        q.executeUpdate();
        em.getTransaction().commit();
    }
}
