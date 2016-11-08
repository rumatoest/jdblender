package jdblender.mybatis;

import jdblender.core.DbConnection;
import jdblender.core.FrameworkRunner;
import jdblender.mybatis.dao.Brand;
import jdblender.mybatis.dao.Model;
import jdblender.mybatis.dao.Series;
import jdblender.mybatis.dao.Spare;
import jdblender.mybatis.mappers.BrandsMapper;
import jdblender.mybatis.mappers.ModelsMapper;
import jdblender.mybatis.mappers.SeriesMapper;
import jdblender.mybatis.mappers.SparesMapper;

import org.apache.ibatis.mapping.Environment;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.apache.ibatis.transaction.TransactionFactory;
import org.apache.ibatis.transaction.jdbc.JdbcTransactionFactory;
import org.h2.jdbcx.JdbcDataSource;

import java.util.Collection;

class RunnerMybatis implements FrameworkRunner {

    SqlSessionFactory sqlSessionFactory;

    @Override
    public void init(DbConnection connection) throws Exception {

        JdbcDataSource ds = new JdbcDataSource();
        ds.setURL(connection.uri);
        ds.setUser(connection.username);
        ds.setPassword(connection.password);

        TransactionFactory transactionFactory = new JdbcTransactionFactory();
        Environment environment = new Environment("jdblender", transactionFactory, ds);

        Configuration cfg = new Configuration(environment);
        cfg.setMapUnderscoreToCamelCase(true);
        cfg.addMapper(BrandsMapper.class);
        cfg.addMapper(SeriesMapper.class);
        cfg.addMapper(ModelsMapper.class);
        cfg.addMapper(SparesMapper.class);

        //Create session
        sqlSessionFactory = new SqlSessionFactoryBuilder().build(cfg);
    }

    <T extends Object> T brandsExec(SqlExec<T, BrandsMapper> callback) {
        try (SqlSession session = sqlSessionFactory.openSession()) {
            BrandsMapper mapper = session.getMapper(BrandsMapper.class);
            T result = callback.execute(mapper);
            if (result instanceof Boolean) {
                session.commit();
            }
            return result;
        }
    }

    <T extends Object> T seriesExec(SqlExec<T, SeriesMapper> callback) {
        try (SqlSession session = sqlSessionFactory.openSession()) {
            SeriesMapper mapper = session.getMapper(SeriesMapper.class);
            T result = callback.execute(mapper);
            if (result instanceof Boolean) {
                session.commit();
            }
            return result;
        }
    }

    <T extends Object> T modelsExec(SqlExec<T, ModelsMapper> callback) {
        try (SqlSession session = sqlSessionFactory.openSession()) {
            ModelsMapper mapper = session.getMapper(ModelsMapper.class);
            T result = callback.execute(mapper);
            if (result instanceof Boolean) {
                session.commit();
            }
            return result;
        }
    }

    <T extends Object> T sparesExec(SqlExec<T, SparesMapper> callback) {
        try (SqlSession session = sqlSessionFactory.openSession()) {
            SparesMapper mapper = session.getMapper(SparesMapper.class);
            T result = callback.execute(mapper);
            if (result instanceof Boolean) {
                session.commit();
            }
            return result;
        }
    }

    @Override
    public void close() throws Exception {
    }

    @Override
    public void createBrand(long id, String name) throws Exception {
        Brand brand = new Brand();
        brand.setId(id);
        brand.setName(name);

        brandsExec(maper -> {
            maper.create(brand);
            return Boolean.TRUE;
        });
    }

    @Override
    public Brand getBrand(long id) throws Exception {
        return brandsExec(m -> m.get(id));
    }

    @Override
    public void createSeries(long id, long brandId, String name) throws Exception {
        Series series = new Series();
        series.setId(id);
        series.setBrandId(brandId);
        series.setName(name);

        seriesExec(m -> {
            m.create(series);
            return Boolean.TRUE;
        });
    }

    @Override
    public Series getSeries(long id) throws Exception {
        return seriesExec(m -> m.get(id));
    }

    @Override
    public Series getSeriesObj(long id) throws Exception {
        return seriesExec(m -> m.getObj(id));
    }

    @Override
    public void createModel(long id, long seriesId, String name) throws Exception {
        Model model = new Model();
        model.setId(id);
        model.setSeriesId(seriesId);
        model.setName(name);
        modelsExec(m -> {
            m.create(model);
            return Boolean.TRUE;
        });
    }

    @Override
    public Model getModel(long id) throws Exception {
        return modelsExec(m -> m.get(id));
    }

    @Override
    public Model getModelObj(long id) throws Exception {
        return modelsExec(m -> m.getObj(id));
    }

    @Override
    public Model getModelObjWithSpares(long id) throws Exception {
        return modelsExec(m -> m.getWithSpares(id));
    }

    @Override
    public void createSpare(long id, long brandId, String name, String label, boolean flag, int num) throws Exception {
        Spare spare = new Spare();
        spare.setId(id);
        spare.setBrandId(brandId);
        spare.setName(name);
        spare.setLabel(label);
        spare.setFlag(flag);
        spare.setNum(num);

        sparesExec(m -> {
            m.create(spare);
            return Boolean.TRUE;
        });
    }

    @Override
    public Spare getSpare(long id) throws Exception {
        return sparesExec(m -> m.get(id));
    }

    @Override
    public Collection<? extends Spare> getSpares(String label, Boolean flag, Integer numFromInclusive, Integer numToInclusive) throws Exception {
        return sparesExec(m -> m.findSpares(label, flag, numFromInclusive, numToInclusive));
    }

    @Override
    public Spare getSpareObj(long id) throws Exception {
        return sparesExec(m -> m.getObj(id));
    }

    @Override
    public void linkModel2Spare(long modelId, long spareId) throws Exception {
        modelsExec(m -> {
            m.link2Spare(modelId, spareId);
            return Boolean.TRUE;
        });
    }

    @Override
    public void linkModel2SpareOptimized(long modelId, long spareId) throws Exception {
        modelsExec(m -> {
            m.link2Spare(modelId, spareId);
            return Boolean.TRUE;
        });
    }

    interface SqlExec<T, U> {

        T execute(U mapper);
    }
}
