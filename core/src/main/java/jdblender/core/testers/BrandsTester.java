package jdblender.core.testers;

import jdblender.core.BlenderApp;
import jdblender.core.FrameworkRunner;
import jdblender.core.Scores;
import jdblender.core.domain.Brand;

import org.apache.commons.lang3.RandomStringUtils;

import java.io.IOException;
import java.util.logging.Logger;

public class BrandsTester {

    private static final Logger LOG = Logger.getLogger(BrandsTester.class.getCanonicalName());

    public static final int ID_MIN = 1;

    private static final int ID_MAX = 10_000;

    private final FrameworkRunner runner;

    private final BlenderApp app;

    public BrandsTester(BlenderApp app, FrameworkRunner runner) throws IOException {
        this.runner = runner;
        this.app = app;
    }

    private int idMax;

    public int getIdMax() {
        if (idMax == 0) {
            if (app.getFactor() > 5) {
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
        LOG.info("Inserting brands");
        return Tester.test(ID_MIN, getIdMax(), id -> insertOne(id));
    }

    long insertOne(int id) throws Exception {
        runner.createBrand(id, RandomStringUtils.randomAscii(16));
        return 1;
    }

    public Scores selectSome() {
        LOG.info("Selecting brands");
        return Tester.test(ID_MIN, getIdMax(), id -> selectOne(id));
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
