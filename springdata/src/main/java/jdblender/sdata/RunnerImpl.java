package jdblender.sdata;

import java.util.ArrayList;
import java.util.Collection;
import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import jdblender.core.DbConnection;
import jdblender.core.FrameworkRunner;
import jdblender.sdata.dao.BrandsDao;
import jdblender.sdata.dao.ModelsDao;
import jdblender.sdata.dao.SeriesDao;
import jdblender.sdata.dao.SparesDao;
import jdblender.sdata.model.Brand;
import jdblender.sdata.model.Model;
import jdblender.sdata.model.Series;
import jdblender.sdata.model.Spare;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class RunnerImpl implements FrameworkRunner {

    @Autowired
    BrandsDao brandsDao;

    @Autowired
    ModelsDao modelsDao;

    @Autowired
    SeriesDao seriesDao;

    @Autowired
    SparesDao sparesDao;

    @Autowired
    JpaContext jpaContext;

    @Override
    public int getFactor() {
        return 1;
    }

    @Override
    public void init(DbConnection connection) throws Exception {
    }

    @Override
    public void close() throws Exception {
    }

    @Override
    public void createBrand(long id, String name) throws Exception {
        brandsDao.save(new jdblender.sdata.model.Brand(id, name));
    }

    @Override
    public Brand getBrand(long id) throws Exception {
        return brandsDao.findOne(id);
    }

    @Override
    public void createSeries(long id, long brandId, String name) throws Exception {
        Brand brand = brandsDao.findOne(brandId);
        seriesDao.save(new Series(id, brand, name));
    }

    @Override
    public Series getSeries(long id) throws Exception {
        return seriesDao.findOne(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Series getSeriesObj(long id) throws Exception {
        Series result = seriesDao.findOne(id);
        result.getBrand().getName();
        return result;
    }

    @Override
    public void createModel(long id, long seriesId, String name) throws Exception {
        Series series = seriesDao.findOne(seriesId);
        modelsDao.save(new Model(id, series, name));
    }

    @Override
    public Model getModel(long id) throws Exception {
        return modelsDao.findOne(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Model getModelObj(long id) throws Exception {
        Model result = modelsDao.findOne(id);
        result.getSeries().getBrand().getName();
        return result;
    }

    @Override
    @Transactional(readOnly = true)
    public Model getModelObjWithSpares(long id) throws Exception {
        Model result = modelsDao.findOne(id);
        result.getSeries().getBrand().getName();
        result.getSpares();
        return result;
    }

    @Override
    public void createSpare(long id, long brandId, String name, String label, boolean flag, int num) throws Exception {
        Spare spare = new Spare();
        spare.setId(id);
        spare.setName(name);
        spare.setLabel(label);
        spare.setFlag(flag);
        spare.setNum(num);
        spare.setBrand(brandsDao.findOne(brandId));

        sparesDao.save(spare);
    }

    @Override
    public Spare getSpare(long id) throws Exception {
        return sparesDao.findOne(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Collection<? extends Spare> getSpares(String label, Boolean flag, Integer numFromInclusive, Integer numToInclusive) throws Exception {
        EntityManager em = jpaContext.getEntityManagerByManagedType(Spare.class);

        CriteriaBuilder cb = em.getCriteriaBuilder();
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
        return em.createQuery(q).getResultList();
    }

    @Override
    @Transactional(readOnly = true)
    public Spare getSpareObj(long id) throws Exception {
        Spare result = sparesDao.findOne(id);
        result.getBrand().getName();
        return result;
    }

    @Override
    @Transactional
    public void linkModel2Spare(long modelId, long spareId) throws Exception {
        Spare spare = sparesDao.findOne(spareId);
        Model model = modelsDao.findOne(modelId);

        model.getSpares().add(spare);

        modelsDao.save(model);
    }

    @Override
    public void linkModel2SpareOptimized(long modelId, long spareId) throws Exception {
        sparesDao.linkSpare2Models(modelId, spareId);
    }
}
