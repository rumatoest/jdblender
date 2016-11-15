package jdblender.mybatis;

import jdblender.core.DbConnection;
import jdblender.core.FrameworkRunner;
import jdblender.mybatis.mappers.BrandsMapper;
import jdblender.mybatis.mappers.ModelsMapper;
import jdblender.mybatis.mappers.SeriesMapper;
import jdblender.mybatis.mappers.SparesMapper;
import jdblender.mybatis.model.Brand;
import jdblender.mybatis.model.Model;
import jdblender.mybatis.model.Series;
import jdblender.mybatis.model.Spare;

import org.apache.ibatis.mapping.Environment;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.apache.ibatis.transaction.TransactionFactory;
import org.apache.ibatis.transaction.jdbc.JdbcTransactionFactory;
import org.h2.jdbcx.JdbcConnectionPool;

import java.util.Collection;

class RunnerMybatis implements FrameworkRunner {

    SqlSessionFactory sqlSessionFactory;

    @Override
    public int getFactor() {
        return 1;
    }

    @Override
    public void init(DbConnection connection) throws Exception {
        JdbcConnectionPool pool = JdbcConnectionPool.create(connection.uri, connection.username, connection.password);

        TransactionFactory transactionFactory = new JdbcTransactionFactory();
        Environment environment = new Environment("jdblender", transactionFactory, pool);

        Configuration cfg = new Configuration(environment);
        cfg.setMapUnderscoreToCamelCase(true);
        cfg.addMapper(BrandsMapper.class);
        cfg.addMapper(SeriesMapper.class);
        cfg.addMapper(ModelsMapper.class);
        cfg.addMapper(SparesMapper.class);

        //Create session
        sqlSessionFactory = new SqlSessionFactoryBuilder().build(cfg);
    }

    @Override
    public void close() throws Exception {
    }

    @Override
    public void createBrand(long id, String name) throws Exception {
        Brand brand = new Brand();
        brand.setId(id);
        brand.setName(name);

        try (SqlSession session = sqlSessionFactory.openSession()) {
            BrandsMapper mapper = session.getMapper(BrandsMapper.class);
            mapper.create(brand);
            session.commit(true);
        }
    }

    @Override
    public Brand getBrand(long id) throws Exception {
        try (SqlSession session = sqlSessionFactory.openSession()) {
            BrandsMapper mapper = session.getMapper(BrandsMapper.class);
            return mapper.get(id);
        }
    }

    @Override
    public void createSeries(long id, long brandId, String name) throws Exception {
        Series series = new Series();
        series.setId(id);
        series.setBrandId(brandId);
        series.setName(name);

        try (SqlSession session = sqlSessionFactory.openSession()) {
            SeriesMapper mapper = session.getMapper(SeriesMapper.class);
            mapper.create(series);
            session.commit(true);
        }
    }

    @Override
    public Series getSeries(long id) throws Exception {
        try (SqlSession session = sqlSessionFactory.openSession()) {
            SeriesMapper mapper = session.getMapper(SeriesMapper.class);
            return mapper.get(id);
        }
    }

    @Override
    public Series getSeriesObj(long id) throws Exception {
        try (SqlSession session = sqlSessionFactory.openSession()) {
            SeriesMapper mapper = session.getMapper(SeriesMapper.class);
            return mapper.getObj(id);
        }
    }

    @Override
    public void createModel(long id, long seriesId, String name) throws Exception {
        Model model = new Model();
        model.setId(id);
        model.setSeriesId(seriesId);
        model.setName(name);

        try (SqlSession session = sqlSessionFactory.openSession()) {
            ModelsMapper mapper = session.getMapper(ModelsMapper.class);
            mapper.create(model);
            session.commit(true);
        }
    }

    @Override
    public Model getModel(long id) throws Exception {
        try (SqlSession session = sqlSessionFactory.openSession()) {
            ModelsMapper mapper = session.getMapper(ModelsMapper.class);
            return mapper.get(id);
        }
    }

    @Override
    public Model getModelObj(long id) throws Exception {
        try (SqlSession session = sqlSessionFactory.openSession()) {
            ModelsMapper mapper = session.getMapper(ModelsMapper.class);
            return mapper.getObj(id);
        }
    }

    @Override
    public Model getModelObjWithSpares(long id) throws Exception {
        try (SqlSession session = sqlSessionFactory.openSession()) {
            ModelsMapper mapper = session.getMapper(ModelsMapper.class);
            return mapper.getWithSpares(id);
        }
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

        try (SqlSession session = sqlSessionFactory.openSession()) {
            SparesMapper mapper = session.getMapper(SparesMapper.class);
            mapper.create(spare);
            session.commit(true);
        }
    }

    @Override
    public Spare getSpare(long id) throws Exception {
        try (SqlSession session = sqlSessionFactory.openSession()) {
            SparesMapper mapper = session.getMapper(SparesMapper.class);
            return mapper.get(id);
        }
    }

    @Override
    public Collection<? extends Spare> getSpares(String label, Boolean flag, Integer numFromInclusive, Integer numToInclusive) throws Exception {
        try (SqlSession session = sqlSessionFactory.openSession()) {
            SparesMapper mapper = session.getMapper(SparesMapper.class);
            return mapper.findSpares(label, flag, numFromInclusive, numToInclusive);
        }
    }

    @Override
    public Spare getSpareObj(long id) throws Exception {
        try (SqlSession session = sqlSessionFactory.openSession()) {
            SparesMapper mapper = session.getMapper(SparesMapper.class);
            return mapper.getObj(id);
        }
    }

    @Override
    public void linkModel2Spare(long modelId, long spareId) throws Exception {
        try (SqlSession session = sqlSessionFactory.openSession()) {
            ModelsMapper mapper = session.getMapper(ModelsMapper.class);
            mapper.link2Spare(modelId, spareId);
            session.commit(true);
        }
    }

    @Override
    public void linkModel2SpareOptimized(long modelId, long spareId) throws Exception {
        try (SqlSession session = sqlSessionFactory.openSession()) {
            ModelsMapper mapper = session.getMapper(ModelsMapper.class);
            mapper.link2Spare(modelId, spareId);
            session.commit(true);
        }
    }
}
