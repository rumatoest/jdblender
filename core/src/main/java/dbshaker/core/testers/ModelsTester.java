package dbshaker.core.testers;

import dbshaker.core.FrameworkRunner;
import dbshaker.core.Scores;
import dbshaker.core.domain.Model;
import dbshaker.core.domain.ModelObj;
import java.util.logging.Logger;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.RandomUtils;

public class ModelsTester {

    private static final Logger LOG = Logger.getLogger(ModelsTester.class.getCanonicalName());

    public static int ID_MIN = 1;

    public static int ID_MAX = 1_000_000;

    static int ID_1 = SeriesTester.ID_MAX / 2;

    static int ID_2 = 200000;

    private final FrameworkRunner runner;

    public ModelsTester(FrameworkRunner runner) {
        this.runner = runner;
    }

    public Scores insertData() {
        LOG.info("Inserting models");
        return Tester.test(ID_MIN, ID_MAX, id -> insertOne(id));
    }

    long insertOne(int id) throws Exception {
        int seriesId = id <= SeriesTester.ID_MAX ? id : RandomUtils.nextInt(SeriesTester.ID_MIN, SeriesTester.ID_MAX + 1);
        runner.createModel(id, seriesId, RandomStringUtils.randomAlphanumeric(16));
        return 1;
    }

    public Scores selectSome() {
        LOG.info("Selecting models");
        return Tester.test(ID_MIN, ID_1, id -> selectOne(id));
    }

    long selectOne(long i) throws Exception {
        long id = RandomUtils.nextLong(ID_MIN, ID_MAX);
        Model model = runner.getModel(id);
        return Tester.codeIt(model.getId(), model.getSeriesId(), model.getName().hashCode());
    }

    public Scores selectSomeObj() {
        LOG.info("Selecting models objects");
        return Tester.test(ID_MIN, ID_1, id -> selectOneObj(id));
    }

    long selectOneObj(long i) throws Exception {
        long id = RandomUtils.nextLong(ID_MIN, ID_MAX);
        ModelObj model = runner.getModelObj(id);
        return Tester.codeIt(model.getId(), model.getName().hashCode(),
            model.getSeries().getId(), model.getSeries().getName().hashCode(),
            model.getSeries().getBrand().getId(),
            model.getSeries().getBrand().getName().hashCode()
        );
    }
}
