package dbshaker.core;

/**
 * Will run code exactly on particular DB framework.
 *
 * @author rumatoest
 */
public interface FrameworkRunner {

    /**
     * Will call it once before tests begin.
     */
    void init(DbConnection connection);

    void createBrand(long id, String name);

    void createModel(long id, long brandId, String name);

    void createModelVariant(long id, long modelId, String name);

    void createSpare(long id, String name);

    void linkSpare2ModelVariant(long spareId, long modelVariantId);
}
