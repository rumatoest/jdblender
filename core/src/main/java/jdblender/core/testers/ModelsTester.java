package jdblender.core.testers;

import jdblender.core.FrameworkRunner;
import jdblender.core.Scores;
import jdblender.core.domain.Model;
import jdblender.core.domain.ModelObj;

import org.apache.commons.lang3.RandomStringUtils;

import java.util.logging.Logger;

public class ModelsTester {

    private static final Logger LOG = Logger.getLogger(ModelsTester.class.getCanonicalName());

    public static int ID_MIN = 1;

    public static int ID_MAX = 250_000;

    private final FrameworkRunner runner;

    public ModelsTester(FrameworkRunner runner) {
        this.runner = runner;
    }

    public Scores insertData() {
        LOG.info("Inserting models");
        return Tester.test(ID_MIN, ID_MAX, id -> insertOne(id));
    }

    long insertOne(int id) throws Exception {
        int seriesId = id % SeriesTester.ID_MAX + 1;
        runner.createModel(id, seriesId, RandomStringUtils.randomAlphanumeric(16));
        if (id % (ID_MAX / 5) == 0) {
            LOG.info("Inserted model " + id);
        }
        return 1;
    }

    public Scores selectSome() {
        LOG.info("Selecting models");
        return Tester.test(ID_MIN, ID_MAX / 2, id -> selectOne(id));
    }

    long selectOne(long id) throws Exception {
        Model model = runner.getModel(id);
        return hashModel(model);
    }

    public Scores selectSomeObj() {
        LOG.info("Selecting models objects");
        return Tester.test(ID_MAX / 2, ID_MAX, id -> selectOneObj(id));
    }

    long selectOneObj(long id) throws Exception {
        ModelObj model = runner.getModelObj(id);
        return hashModelObj(model);
    }

    public static long hashModel(Model model) {
        return Tester.codeIt(model.getId(), model.getSeriesId(), model.getName().hashCode());
    }

    public static long hashModelObj(ModelObj model) {
        return Tester.codeIt(
            model.getId(),
            model.getName().hashCode(),
            SeriesTester.hashSeriesObj(model.getSeries())
        );
    }
}
