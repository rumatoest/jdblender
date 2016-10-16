package dbshaker.core.testers;

import dbshaker.core.FrameworkRunner;
import dbshaker.core.Scores;
import dbshaker.core.domain.Series;
import dbshaker.core.domain.SeriesObj;
import java.util.logging.Logger;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.RandomUtils;

public class SeriesTester {

    private static final Logger LOG = Logger.getLogger(SeriesTester.class.getCanonicalName());

    public static int ID_MIN = 1;

    public static int ID_MAX = 100000;

    private final FrameworkRunner runner;

    public SeriesTester(FrameworkRunner runner) {
        this.runner = runner;
    }

    public Scores insertData() {
        LOG.info("Inserting series");
        return Tester.test(ID_MIN, ID_MAX, id -> insertOne(id));
    }

    long insertOne(int id) throws Exception {
        int brandId = RandomUtils.nextInt(BrandsTester.ID_MIN, BrandsTester.ID_MAX + 1);
        runner.createSeries(id, brandId, RandomStringUtils.randomAlphanumeric(16));
        return 1;
    }

    public Scores selectSome() {
        LOG.info("Selecting series");
        return Tester.test(ID_MIN, ID_MAX / 2, id -> selectOne(id));
    }

    long selectOne(int id) throws Exception {
        Series s = runner.getSeries(id);
        return Tester.codeIt(s.getId(), s.getName().hashCode());
    }

    public Scores selectSomeObj() {
        LOG.info("Selecting series objects");
        return Tester.test(ID_MAX / 2 + 1, ID_MAX, id -> selectOneObj(id));
    }

    long selectOneObj(int id) throws Exception {
        SeriesObj s = runner.getSeriesObj(id);

        return Tester.codeIt(s.getId(), s.getName().hashCode(),
            s.getBrand().getId(), s.getBrand().getName().hashCode());
    }
}
