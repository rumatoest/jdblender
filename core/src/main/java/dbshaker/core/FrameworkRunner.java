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
    void init(DbConnection connection) throws Exception;

    /**
     * All test completed.
     * Runner must be close all resources here.
     */
    void close() throws Exception;

    void createSeries(long id, long brandId, String name) throws Exception;

    void createBrand(long id, String name) throws Exception;

    void createModel(long id, long brandId, String name) throws Exception;

    void createModelVariant(long id, long modelId, String name) throws Exception;

    void createSpare(long id, String name) throws Exception;

    void linkSpare2ModelVariant(long spareId, long modelVariantId) throws Exception;

    Brand getBrand(long id) throws Exception;
}
