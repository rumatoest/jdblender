package dbshaker.core;

import dbshaker.core.domain.Brand;

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

    /**
     * All test completed.
     * Runner must be close all resources here.
     */
    void close();

    void createBrand(long id, String name);

    void createModel(long id, long brandId, String name);

    void createModelVariant(long id, long modelId, String name);

    void createSpare(long id, String name);

    void linkSpare2ModelVariant(long spareId, long modelVariantId);

    Brand getBrand(long id);
}
