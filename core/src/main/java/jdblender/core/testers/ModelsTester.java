package jdblender.core.testers;

import jdblender.core.BlenderApp;
import jdblender.core.FrameworkRunner;
import jdblender.core.Scores;
import jdblender.core.domain.Model;
import jdblender.core.domain.ModelObj;

import org.apache.commons.lang3.RandomStringUtils;

import java.util.logging.Logger;

public class ModelsTester {

    private static final Logger LOG = Logger.getLogger(ModelsTester.class.getCanonicalName());

    public final static int ID_MIN = 1;

    private final static int ID_MAX = 250_000;

    private final BlenderApp app;

    private final FrameworkRunner runner;

    private final SeriesTester seriesTst;

    public ModelsTester(BlenderApp app, FrameworkRunner runner, SeriesTester seriesTst) {
        this.app = app;
        this.runner = runner;
        this.seriesTst = seriesTst;
    }

    private int idMax;

    public int getIdMax() {
        if (idMax == 0) {
            if (app.getFactor() > 10) {
                idMax = 20_000;
            } else if (app.getFactor() > 1) {
                idMax = ID_MAX / app.getFactor();
            } else {
                idMax = ID_MAX;
            }
        }
        return idMax;
    }

    public Scores insertData() {
        LOG.info("Inserting models");
        return Tester.test(ID_MIN, getIdMax(), id -> insertOne(id));
    }

    long insertOne(int id) throws Exception {
        int seriesId = id % seriesTst.getIdMax() + 1;
        runner.createModel(id, seriesId, RandomStringUtils.randomAlphanumeric(16));
        if (id % (getIdMax() / 5) == 0) {
            LOG.info("Inserted model " + id);
        }
        return 1;
    }

    public Scores selectSome() {
        LOG.info("Selecting models");
        return Tester.test(ID_MIN, getIdMax() / 2, id -> selectOne(id));
    }

    long selectOne(long id) throws Exception {
        Model model = runner.getModel(id);
        return hashModel(model);
    }

    public Scores selectSomeObj() {
        LOG.info("Selecting models objects");
        return Tester.test(getIdMax() / 2, getIdMax(), id -> selectOneObj(id));
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
