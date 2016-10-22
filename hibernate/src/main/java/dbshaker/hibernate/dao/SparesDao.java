package dbshaker.hibernate.dao;

import dbshaker.hibernate.model.Spare;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

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
        Session s = sf.getCurrentSession();
        Transaction tx = s.beginTransaction();
        try {
            Spare ret = s.get(Spare.class, id);
            ret.getBrand().getId();
            return ret;
        } finally {
            tx.commit();
        }
    }

    public Spare getByPkObj(long id) {
        Session s = sf.getCurrentSession();
        Transaction tx = s.beginTransaction();
        try {
            Spare ret = s.get(Spare.class, id);
            ret.getBrand().getId();
            return ret;
        } finally {
            tx.commit();
        }
    }
}
