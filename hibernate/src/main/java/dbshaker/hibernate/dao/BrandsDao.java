package dbshaker.hibernate.dao;

import dbshaker.hibernate.model.Brand;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

public class BrandsDao {

    private SessionFactory sf;

    public BrandsDao(SessionFactory sf) {
        this.sf = sf;
    }

    public Brand getByPk(long id) {
        Session s = sf.getCurrentSession();
        Transaction tx = s.beginTransaction();
        try {
            return s.get(Brand.class, id);
        } finally {
            tx.commit();
        }
    }

    public void persist(Brand brand) {
        Session s = sf.getCurrentSession();
        Transaction tx = s.beginTransaction();
        s.save(brand);
        tx.commit();
    }

}
