package jdblender.core.testers;

import jdblender.core.FrameworkRunner;
import jdblender.core.Scores;
import jdblender.core.domain.Series;
import jdblender.core.domain.SeriesObj;
import java.util.logging.Logger;
import jdblender.core.BlenderApp;

import org.apache.commons.lang3.RandomStringUtils;

public class SeriesTester {

    private static final Logger LOG = Logger.getLogger(SeriesTester.class.getCanonicalName());

    public static final int ID_MIN = 1;

    private static final int ID_MAX = 50_000;

    private final BlenderApp app;

    private final FrameworkRunner runner;

    private final BrandsTester brandsTst;

    public SeriesTester(BlenderApp app, FrameworkRunner runner, BrandsTester bt) {
        this.runner = runner;
        this.app = app;
        this.brandsTst = bt;
    }

    private int idMax;

    public int getIdMax() {
        if (idMax == 0) {
            if (app.getFactor() > 10) {
                idMax = 5000;
            } else if (app.getFactor() > 1) {
                idMax = ID_MAX / app.getFactor();
            } else {
                idMax = ID_MAX;
            }
        }
        return idMax;
    }

    public Scores insertData() {
        LOG.info("Inserting series");
        return Tester.test(ID_MIN, getIdMax(), id -> insertOne(id));
    }

    long insertOne(int id) throws Exception {
        int brandId = id % brandsTst.getIdMax() + 1;
        runner.createSeries(id, brandId, RandomStringUtils.randomAlphanumeric(16));
        return 1;
    }

    public Scores selectSome() {
        LOG.info("Selecting series");
        return Tester.test(ID_MIN, getIdMax() / 2, id -> selectOne(id));
    }

    long selectOne(int id) throws Exception {
        Series s = runner.getSeries(id);
        return hashSeries(s);
    }

    public Scores selectSomeObj() {
        LOG.info("Selecting series objects");
        return Tester.test(getIdMax() / 2, getIdMax(), id -> selectOneObj(id));
    }

    long selectOneObj(int id) throws Exception {
        try {
            SeriesObj s = runner.getSeriesObj(id);
            return hashSeriesObj(s);
        } catch (Exception ex) {
            LOG.severe("ID: " + id);
            throw ex;
        }
    }

    public static long hashSeries(Series series) {
        return Tester.codeIt(series.getId(), series.getName().hashCode());
    }

    public static long hashSeriesObj(SeriesObj series) {
        return Tester.codeIt(
            series.getId(),
            series.getName().hashCode(),
            BrandsTester.hashBrand(series.getBrand())
        );
    }
}
