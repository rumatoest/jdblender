package dbshaker.core.testers;

import dbshaker.core.FrameworkRunner;
import dbshaker.core.Scores;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.RandomUtils;

public class SeriesTester {

    public static int ID_MIN = 1;

    public static int ID_MAX = 100000;

    private final FrameworkRunner runner;

    public SeriesTester(FrameworkRunner runner) {
        this.runner = runner;
    }

    public Scores insertData() {
        return Tester.test(ID_MIN, ID_MAX, id -> insertOne(id));
    }

    void insertOne(int id) throws Exception {
        int brandId = RandomUtils.nextInt(BrandsTester.ID_MIN, BrandsTester.ID_MAX + 1);
        runner.createSeries(id, brandId, RandomStringUtils.randomAlphanumeric(16));
    }

}
