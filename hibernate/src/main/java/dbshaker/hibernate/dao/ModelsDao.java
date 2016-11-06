package dbshaker.hibernate.dao;

import dbshaker.hibernate.model.Model;
import dbshaker.hibernate.model.Spare;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.NativeQuery;

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
        try (Session s = sf.openSession()) {
            return s.get(Model.class, id);
        }
    }

    public Model getByPkObj(long id) {
        try (Session s = sf.openSession()) {
            Model ret = s.get(Model.class, id);
            ret.getSeries().getBrand().getId();
            return ret;
        }
    }

    public Model getByPkObjSpares(long id) {
        try (Session s = sf.openSession()) {
            Model ret = s.get(Model.class, id);
            ret.getSeries().getBrand().getId();
            ret.getSpares().isEmpty();
            return ret;
        }
    }

    public void link2Spare(long modelId, long spareId) {
        try (Session s = sf.openSession()) {
            Model model = s.get(Model.class, modelId);
            model.getSpares().add(s.get(Spare.class, spareId));
            Transaction tx = s.beginTransaction();
            s.save(model);
            tx.commit();
        }
    }

    public void link2SpareFast(long modelId, long spareId) {
        try (Session s = sf.openSession()) {
            NativeQuery nq = s.createNativeQuery("INSERT INTO spare_to_model (spare_id, model_id) VALUES (?, ?)");
            nq.setParameter(1, spareId);
            nq.setParameter(2, modelId);
            Transaction tx = s.beginTransaction();
            nq.executeUpdate();
            tx.commit();
        }
    }
}
