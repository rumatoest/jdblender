package jdblender.hibernate.dao;

import jdblender.hibernate.model.Spare;
import java.util.List;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;

public class SparesDao {

    private final SessionFactory sf;

    public SparesDao(SessionFactory sf) {
        this.sf = sf;
    }

    public void persist(Spare spare) {
        Session s = sf.getCurrentSession();
        Transaction tx = s.beginTransaction();
        s.save(spare);
        tx.commit();
    }

    public Spare getByPk(long id) {
        try (Session s = sf.openSession()) {
            return s.get(Spare.class, id);
        }
    }

    public Spare getByPkObj(long id) {
        try (Session s = sf.openSession()) {
            Spare ret = s.get(Spare.class, id);
            ret.getBrand().getId();
            return ret;
        }
    }

    public List<Spare> findSpares(String label, Boolean flag, Integer numFromInclusive, Integer numToInclusive) {
        try (Session s = sf.openSession()) {
            Criteria cq = s.createCriteria(Spare.class);

            if (label != null) {
                cq.add(Restrictions.eq("label", label));
            }

            if (flag != null) {
                cq.add(Restrictions.eq("flag", flag));
            }

            if (numFromInclusive != null) {
                if (numToInclusive == null) {
                    cq.add(Restrictions.eq("num", numFromInclusive));
                } else {
                    cq.add(Restrictions.conjunction(Restrictions.ge("num", numFromInclusive), Restrictions.ge("num", numToInclusive)));
                }
            }
            return cq.list();
        }
    }
}
