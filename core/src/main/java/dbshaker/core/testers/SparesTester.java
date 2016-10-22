package dbshaker.core.testers;

import dbshaker.core.FrameworkRunner;
import dbshaker.core.Scores;
import dbshaker.core.domain.Spare;
import dbshaker.core.domain.SpareObj;

import org.apache.commons.lang3.RandomStringUtils;

import java.util.logging.Logger;

public class SparesTester {

    private static final Logger LOG = Logger.getLogger(SparesTester.class.getCanonicalName());

    public static int ID_MIN = 1;

    public static int ID_MAX = 1_000_000;

    private final FrameworkRunner runner;

    public SparesTester(FrameworkRunner runner) {
        this.runner = runner;
    }

    public Scores insertData() {
        LOG.info("Inserting spares");
        return Tester.test(ID_MIN, ID_MAX, id -> insertOne(id));
    }

    long insertOne(int id) throws Exception {
        int brandId = id % BrandsTester.ID_MAX + 1;
        int num = id % 200 + 1;
        boolean flag = id % 2 == 0;
        char[] label = new char[]{(char)(id % 26 + 65), (char)((ID_MAX - id) % 26 + 65)};

        runner.createSpare(
            id,
            brandId,
            RandomStringUtils.randomAlphanumeric(16),
            new String(label),
            flag,
            num
        );

        if (id % (ID_MAX / 5) == 0) {
            LOG.info("Inserted spare " + id);
        }
        return 1;
    }

    public Scores selectSome() {
        LOG.info("Selecting spares");
        return Tester.test(ID_MIN, ID_MAX / 2, id -> selectOne(id));
    }

    long selectOne(int id) throws Exception {
        Spare spare = runner.getSpare(id);
        return hashSpare(spare);
    }

    public Scores selectSomeObj() {
        LOG.info("Selecting spares OBJ");
        return Tester.test(ID_MAX / 2, ID_MAX, id -> selectOneObj(id));
    }

    long selectOneObj(int id) throws Exception {
        SpareObj spare = runner.getSpareObj(id);
        return hashSpareObj(spare);
    }

    public static long hashSpare(Spare spare) {
        return Tester.codeIt(
            spare.getId(),
            spare.getBrandId(),
            spare.getName().hashCode(),
            spare.getLabel().hashCode(),
            spare.getFlag() ? 1 : 0,
            spare.getNum()
        );
    }

    public static long hashSpareObj(SpareObj spare) {
        return Tester.codeIt(
            spare.getId(),
            spare.getName().hashCode(),
            spare.getLabel().hashCode(),
            spare.getFlag() ? 1 : 0,
            spare.getNum(),
            BrandsTester.hashBrand(spare.getBrand())
        );
    }
}
