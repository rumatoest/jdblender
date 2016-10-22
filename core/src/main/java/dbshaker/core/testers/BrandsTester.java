package dbshaker.core.testers;

import dbshaker.core.FrameworkRunner;
import dbshaker.core.Scores;
import dbshaker.core.domain.Brand;

import org.apache.commons.lang3.RandomStringUtils;

import java.io.IOException;
import java.util.logging.Logger;

public class BrandsTester {

    private static final Logger LOG = Logger.getLogger(BrandsTester.class.getCanonicalName());

    public static int ID_MIN = 1;

    public static int ID_MAX = 10_000;

    private final FrameworkRunner runner;

    public BrandsTester(FrameworkRunner runner) throws IOException {
        this.runner = runner;
    }

    public Scores insertData() {
        LOG.info("Inserting brands");
        return Tester.test(ID_MIN, ID_MAX, id -> insertOne(id));
    }

    long insertOne(int id) throws Exception {
        runner.createBrand(id, RandomStringUtils.randomAscii(16));
        return 1;
    }

    public Scores selectSome() {
        LOG.info("Selecting brands");
        return Tester.test(ID_MIN, ID_MAX, id -> selectOne(id));
    }

    long selectOne(int id) throws Exception {
        Brand brand = runner.getBrand(id);
        return brand.getId() / 2 + brand.getName().hashCode() / 2;
    }

    public static long hashBrand(Brand brand) {
        return Tester.codeIt(
            brand.getId(),
            brand.getName().hashCode()
        );
    }
}
