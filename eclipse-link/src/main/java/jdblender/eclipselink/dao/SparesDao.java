package jdblender.eclipselink.dao;

import jdblender.eclipselink.model.Spare;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

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

    public List<Spare> findSpares(String label, Boolean flag, Integer numFromInclusive, Integer numToInclusive) {
        CriteriaBuilder cb = emf.getCriteriaBuilder();
        CriteriaQuery<Spare> q = cb.createQuery(Spare.class);
        Root<Spare> root = q.from(Spare.class);

        ArrayList<Predicate> where = new ArrayList<>(5);
        if (label != null) {
            where.add(cb.equal(root.get("label"), label));
        }

        if (flag != null) {
            where.add(cb.equal(root.get("flag"), flag));
        }

        if (numFromInclusive != null) {
            if (numToInclusive == null) {
                where.add(cb.equal(root.get("num"), numFromInclusive));
            } else {
                where.add(cb.and(cb.ge(root.get("num"), numFromInclusive), cb.le(root.get("num"), numToInclusive)));
            }
        }

        q.where(where.toArray(new Predicate[0]));
        return emf.createEntityManager().createQuery(q).getResultList();
    }
}
