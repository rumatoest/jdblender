package jdblender.hibernate.dao;

import jdblender.hibernate.model.Brand;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

public class BrandsDao {

    private SessionFactory sf;

    public BrandsDao(SessionFactory sf) {
        this.sf = sf;
    }

    public Brand getByPk(long id) {
        try (Session s = sf.openSession()) {
            return s.get(Brand.class, id);
        }
    }

    public void persist(Brand brand) {
        Session s = sf.getCurrentSession();
        Transaction tx = s.beginTransaction();
        s.save(brand);
        tx.commit();
    }

}
