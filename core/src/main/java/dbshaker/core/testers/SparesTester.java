package dbshaker.core.testers;

import dbshaker.core.FrameworkRunner;
import dbshaker.core.Scores;
import dbshaker.core.domain.ModelObj;
import dbshaker.core.domain.Spare;
import dbshaker.core.domain.SpareObj;

import org.apache.commons.lang3.RandomStringUtils;

import java.util.Collection;
import java.util.logging.Logger;

public class SparesTester {

    private static final Logger LOG = Logger.getLogger(SparesTester.class.getCanonicalName());

    public static int ID_MIN = 1;

    public static int ID_MAX = 1_000_000;

    public static int LINKS_MAX = 2_000_000;

    public static int RATIO = ID_MAX / ModelsTester.ID_MAX;

    static int MAX_NUM_RANGE = 5000;

    static char[] CHARS = new char[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
        'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z',
        'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z',
        'À', 'Á', 'Â', 'Ã', 'Ä', 'Å', 'Æ', 'Ç', 'È', 'É', 'Ê', 'Ë', 'Ì', 'Í', 'Î', 'Ï', 'Ð', 'Ñ', 'Ò', 'Ó', 'Ô', 'Õ', 'Ö', 'Ø', 'Ù', 'Ú',
        'Û', 'Ü', 'Ý', 'Þ', 'ß', 'à', 'á', 'â', 'ã', 'ä', 'å', 'æ', 'ç', 'è', 'é', 'ê', 'ë', 'ì', 'í', 'î', 'ï', 'ð', 'ñ', 'ò', 'ó', 'ô',
        'õ', 'ö', 'ø', 'ù', 'ú', 'û', 'ü', 'ý', 'þ', 'ÿ'};

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
        int num = id % MAX_NUM_RANGE + 1;
        boolean flag = id % 2 == 0;

        runner.createSpare(
            id,
            brandId,
            RandomStringUtils.randomAlphanumeric(16),
            new String(getLabelChars(id)),
            flag,
            num
        );

        if (id % (ID_MAX / 5) == 0) {
            LOG.info("Inserted spare " + id);
        }
        return 1;
    }

    char[] getLabelChars(int idx) {
        return new char[]{CHARS[idx % CHARS.length], CHARS[(ID_MAX - idx) % CHARS.length]};
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

    public Scores insertLinks() {
        LOG.info("Insertings spares<->models m2m");
        return Tester.test(ID_MIN, 25000, id -> insertLink(id));
    }

    long insertLink(int idx) throws Exception {
        long modelId = linkModelId(idx);
        long spareId = linkSpareId(idx);

        runner.linkModel2Spare(modelId, spareId);
        return 1;
    }

    public Scores insertLinksFast() {
        LOG.info("Insertings spares<->models m2m optimized");
        return Tester.test(25001, LINKS_MAX, id -> insertLinkFast(id));
    }

    long insertLinkFast(int idx) throws Exception {
        long modelId = linkModelId(idx);
        long spareId = linkSpareId(idx);

        runner.linkModel2SpareOptimized(modelId, spareId);

        if (idx % (LINKS_MAX / 5) == 0) {
            LOG.info("Inserted M2M " + idx);
        }

        return 1;
    }

    long linkModelId(int idx) {
        return idx % ModelsTester.ID_MAX + 1;
    }

    long linkSpareId(int idx) {
        int pass = idx / ModelsTester.ID_MAX;

        long modelId = linkModelId(idx);
        return ((modelId - 1) * RATIO + pass - 1) % ID_MAX + 1;
    }

    long linkSpareFirstId(int modelId) {
        return ((modelId - 1) * RATIO - 1) % ID_MAX + 1;
    }

    public Scores selectSomeObjWithLinks() {
        LOG.info("Selecting model OBJ + spares");
        return Tester.test(ModelsTester.ID_MIN, ModelsTester.ID_MAX/2, id -> selectModelObjWithLinks(id));
    }

    public long selectModelObjWithLinks(int idx) throws Exception {
        ModelObj mod = runner.getModelObjWithSpares(idx);
        long spareFist = linkSpareFirstId(idx);
        long spareLast = spareFist + RATIO;

        int matched = 0;
        long hash = 0L;
        for (SpareObj spare : mod.getSpares()) {
            hash = Tester.codeIt(hash, hashSpareObj(spare));
            if (spare.getId() >= spareFist && spare.getId() <= spareLast) {
                matched++;
            }
        }

        if (matched < RATIO) {
            throw new Exception("Found " + matched + " spares but expected at least " + RATIO
                + " model_id " + mod.getId());
        }

        return Tester.codeIt(ModelsTester.hashModelObj(mod), hash);
    }

    public Scores selectSpares() {
        LOG.info("Selecting spares with dynamic query");
        return Tester.test(37, 20_000, id -> selectRandomSpares(id));
    }

    public long selectRandomSpares(int idx) throws Exception {
        if (idx % 2000 == 0) {
            LOG.info("Dynamic query " + idx);
        }

        Boolean flag;
        if (idx % 19 == 0) {
            flag = null;
        } else {
            flag = idx % 2 == 0;
        }
        String label;
        if (flag != null && idx % 23 == 0) {
            label = null;
        } else {
            label = new String(getLabelChars(idx));
        }

        Integer numFrom = idx % MAX_NUM_RANGE + 1;
        if (flag != null && label != null && idx % 1109 == 0) {
            numFrom = null;
        }

        Integer numTo = null;
        if (idx % 199 == 0 && numFrom != null && flag != null && label != null) {
            numTo = numFrom + idx % 5 + 1;
        }

        Collection<? extends Spare> spares = runner.getSpares(label, flag, numFrom, numTo);
        long result = 0L;

        /*
        if (numFrom == null) {
            LOG.info("Count:" + spares.size() + " for f:" + flag + " label:" + label + " num:" + numFrom);
        }
        if (numTo != null) {
            LOG.info("Count:" + spares.size() + " for f:" + flag + " label:" + label + " num:" + numFrom);
        }
         */
        if (spares.isEmpty()) {
            LOG.info("Count:" + spares.size() + " for f:" + flag + " label:" + label + " num:" + numFrom);
        }
        for (Spare s : spares) {
            result = Tester.codeIt(result, hashSpare(s));
        }
        return result;
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
