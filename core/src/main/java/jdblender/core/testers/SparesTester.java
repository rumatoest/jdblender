package jdblender.core.testers;

import jdblender.core.BlenderApp;
import jdblender.core.FrameworkRunner;
import jdblender.core.Scores;
import jdblender.core.domain.ModelObj;
import jdblender.core.domain.Spare;
import jdblender.core.domain.SpareObj;

import org.apache.commons.lang3.RandomStringUtils;

import java.util.Collection;
import java.util.logging.Logger;

public class SparesTester {

    private static final Logger LOG = Logger.getLogger(SparesTester.class.getCanonicalName());

    public static final int ID_MIN = 1;

    private static final int ID_MAX = 1_000_000;

    private static final int LINKS_MAX = 2_000_000;

    private static final int MAX_NUM_RANGE = 5000;

    static char[] CHARS = new char[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
        'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z',
        'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z',
        'À', 'Á', 'Â', 'Ã', 'Ä', 'Å', 'Æ', 'Ç', 'È', 'É', 'Ê', 'Ë', 'Ì', 'Í', 'Î', 'Ï', 'Ð', 'Ñ', 'Ò', 'Ó', 'Ô', 'Õ', 'Ö', 'Ø', 'Ù', 'Ú',
        'А', 'Б', 'В', 'Г', 'Д', 'Е', 'Ё', 'Ж', 'З', 'И', 'Й', 'К', 'Л', 'М', 'Н', 'О', 'П', 'Р', 'С', 'Т', 'У', 'Ф', 'Х', 'Ц', 'Ч', 'Ш', 'Щ', 'Ъ', 'Ы', 'Ь', 'Э', 'Ю', 'Я',
        'а', 'б', 'в', 'г', 'д', 'е', 'ё', 'ж', 'з', 'и', 'й', 'к', 'л', 'м', 'н', 'о', 'п', 'р', 'с', 'т', 'у', 'ф', 'х', 'ц', 'ч', 'ш', 'щ', 'ъ', 'ы', 'ь', 'э', 'ю', 'я',
        'Û', 'Ü', 'Ý', 'Þ', 'ß', 'à', 'á', 'â', 'ã', 'ä', 'å', 'æ', 'ç', 'è', 'é', 'ê', 'ë', 'ì', 'í', 'î', 'ï', 'ð', 'ñ', 'ò', 'ó', 'ô',
        'õ', 'ö', 'ø', 'ù', 'ú', 'û', 'ü', 'ý', 'þ', 'ÿ'};

    private final BlenderApp app;

    private final FrameworkRunner runner;

    private final BrandsTester brandsTst;

    private final ModelsTester modelsTst;

    public SparesTester(BlenderApp app, FrameworkRunner runner, BrandsTester brandsTst, ModelsTester modelsTst) {
        this.app = app;
        this.runner = runner;
        this.brandsTst = brandsTst;
        this.modelsTst = modelsTst;
    }

    private int idMax;

    public int getIdMax() {
        if (idMax == 0) {
            if (app.getFactor() > 50) {
                idMax = 20_000;
            } else if (app.getFactor() > 1) {
                idMax = ID_MAX / app.getFactor();
            } else {
                idMax = ID_MAX;
            }
        }
        return idMax;
    }

    private int linksMax;

    public int getLinksMax() {
        if (linksMax == 0) {
            if (app.getFactor() > 50) {
                linksMax = 40_000;
            } else if (app.getFactor() > 1) {
                linksMax = LINKS_MAX / app.getFactor();
            } else {
                linksMax = LINKS_MAX;
            }
        }
        return linksMax;
    }

    private int numRange;

    public int getNumRange() {
        if (numRange == 0) {
            numRange = app.getFactor() > 1 ? MAX_NUM_RANGE / app.getFactor() : MAX_NUM_RANGE;
        }
        return numRange;
    }

    private int ratio;

    public int getRatio() {
        if (ratio == 0) {
            ratio = getLinksMax() / modelsTst.getIdMax();
        }
        return ratio;
    }

    public Scores insertData() {
        LOG.info("Inserting spares");
        return Tester.test(ID_MIN, getIdMax(), id -> insertOne(id));
    }

    long insertOne(int id) throws Exception {
        int brandId = id % brandsTst.getIdMax() + 1;
        int num = id % getNumRange() + 1;
        boolean flag = id % 2 == 0;

        runner.createSpare(
            id,
            brandId,
            RandomStringUtils.randomAlphanumeric(16),
            new String(getLabelChars(id)),
            flag,
            num
        );

        if (id % (getIdMax() / 5) == 0) {
            LOG.info("Inserted spare " + id);
        }
        return 1;
    }

    char[] getLabelChars(int idx) {
        return new char[]{CHARS[idx % CHARS.length], CHARS[(getIdMax() - idx) % CHARS.length]};
    }

    public Scores selectSome() {
        LOG.info("Selecting spares");
        return Tester.test(ID_MIN, getIdMax() / 2, id -> selectOne(id));
    }

    long selectOne(int id) throws Exception {
        Spare spare = runner.getSpare(id);
        return hashSpare(spare);
    }

    public Scores selectSomeObj() {
        LOG.info("Selecting spares OBJ");
        return Tester.test(getIdMax() / 2, getIdMax(), id -> selectOneObj(id));
    }

    long selectOneObj(int id) throws Exception {
        SpareObj spare = runner.getSpareObj(id);
        return hashSpareObj(spare);
    }

    int slowInsertLinks() {
        return getLinksMax() < 50_000 ? Math.round((float)getLinksMax() / 2) : 25_000;
    }

    public Scores insertLinks() {
        LOG.info("Insertings spares<->models m2m");

        return Tester.test(ID_MIN, slowInsertLinks(), id -> insertLink(id));
    }

    long insertLink(int idx) throws Exception {
        long modelId = linkModelId(idx);
        long spareId = linkSpareId(idx);

        runner.linkModel2Spare(modelId, spareId);
        return 1;
    }

    public Scores insertLinksFast() {
        LOG.info("Insertings spares<->models m2m optimized");
        return Tester.test(slowInsertLinks() + 1, getLinksMax(), id -> insertLinkFast(id));
    }

    long insertLinkFast(int idx) throws Exception {
        long modelId = linkModelId(idx);
        long spareId = linkSpareId(idx);

        runner.linkModel2SpareOptimized(modelId, spareId);

        if (idx % (getLinksMax() / 5) == 0) {
            LOG.info("Inserted M2M " + idx);
        }

        return 1;
    }

    long linkModelId(int idx) {
        return idx % modelsTst.getIdMax() + 1;
    }

    long linkSpareId(int idx) {
        int pass = idx / modelsTst.getIdMax();

        long modelId = linkModelId(idx);
        long id = ((modelId - 1) * getRatio() + pass - 1) % getIdMax() + 1;
        return id;
    }

    long linkSpareFirstId(int modelId) {
        return ((modelId - 1) * getRatio() - 1) % getIdMax() + 1;
    }

    public Scores selectSomeObjWithLinks() {
        LOG.info("Selecting model OBJ + spares");
        return Tester.test(ModelsTester.ID_MIN, modelsTst.getIdMax() / 2, id -> selectModelObjWithLinks(id));
    }

    public long selectModelObjWithLinks(int idx) throws Exception {
        if (idx % 20_000 == 0) {
            LOG.info("Model with spares query " + idx);
        }

        ModelObj mod = runner.getModelObjWithSpares(idx);
        long spareFist = linkSpareFirstId(idx);
        long spareLast = spareFist + getRatio();

        int matched = 0;
        long hash = 0L;
        for (SpareObj spare : mod.getSpares()) {
            hash = Tester.codeIt(hash, hashSpareObj(spare));
            if (spare.getId() >= spareFist && spare.getId() <= spareLast) {
                matched++;
            }
        }

        if (matched < getRatio()) {
            throw new Exception("Found " + matched + " spares but expected at least " + getRatio()
                + " model_id " + mod.getId());
        }

        return Tester.codeIt(ModelsTester.hashModelObj(mod), hash);
    }

    public Scores selectSpares() {
        LOG.info("Selecting spares with dynamic query");
        int idTo = getLinksMax() / 100;
        if (idTo < 6000) {
            idTo = 6000;
        }

        return Tester.test(37, idTo, id -> selectRandomSpares(id));
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

        Integer numFrom = idx % getNumRange() + 1;
        if (flag != null && label != null && idx % 1109 == 0) {
            numFrom = null;
        }

        Integer numTo = null;
        if (idx % 5333 == 0 && numFrom != null && flag != null && label != null) {
            numTo = numFrom + idx % 3 + 1;
        }

        Collection<? extends Spare> spares = runner.getSpares(label, flag, numFrom, numTo);
        long result = 0L;

        /*
        if (numFrom == null) {
            LOG.info("Count:" + spares.size() + " for f:" + flag + " label:" + label + " num:" + numFrom);
        }
        if (RandomUtils.nextInt(0, 500) == 0) {
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
