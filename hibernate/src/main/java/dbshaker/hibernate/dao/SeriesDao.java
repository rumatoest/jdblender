package dbshaker.hibernate.dao;

import dbshaker.hibernate.model.Series;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

public class SeriesDao {

    private final SessionFactory sf;

    public SeriesDao(SessionFactory sf) {
        this.sf = sf;
    }

    public void persist(Series series) {
        Session s = sf.getCurrentSession();
        Transaction tx = s.beginTransaction();
        s.save(series);
        tx.commit();
    }

    public Series getByPk(long id) {
        try (Session s = sf.openSession()) {
            return s.get(Series.class, id);
        }
    }

    public Series getByPkObj(long id) {
        try (Session s = sf.openSession()) {
            Series ret = s.get(Series.class, id);
            ret.getBrand().getId();
            return ret;
        }
    }
}
