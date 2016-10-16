package dbshaker.hibernate.dao;

import dbshaker.hibernate.model.Model;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

public class ModelsDao {

    private final SessionFactory sf;

    public ModelsDao(SessionFactory sf) {
        this.sf = sf;
    }

    public void persist(Model model) {
        Session s = sf.getCurrentSession();
        Transaction tx = s.beginTransaction();
        s.save(model);
        tx.commit();
    }

    public Model getByPk(long id) {
        Session s = sf.getCurrentSession();
        Transaction tx = s.beginTransaction();
        try {
            Model ret = s.get(Model.class, id);
            ret.getSeries().getBrand().getId();
            return ret;
        } finally {
            tx.commit();
        }
    }

    public Model getByPkObj(long id) {
        Session s = sf.getCurrentSession();
        Transaction tx = s.beginTransaction();
        try {
            Model ret = s.get(Model.class, id);
            ret.getSeries().getBrand().getId();
            return ret;
        } finally {
            tx.commit();
        }
    }
}
