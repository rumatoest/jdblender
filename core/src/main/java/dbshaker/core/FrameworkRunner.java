package dbshaker.core;

import dbshaker.core.domain.Brand;
import dbshaker.core.domain.Model;
import dbshaker.core.domain.ModelObj;
import dbshaker.core.domain.Series;
import dbshaker.core.domain.SeriesObj;

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

    void createBrand(long id, String name) throws Exception;

    Brand getBrand(long id) throws Exception;

    void createSeries(long id, long brandId, String name) throws Exception;

    Series getSeries(long id) throws Exception;

    SeriesObj getSeriesObj(long id) throws Exception;

    void createModel(long id, long seriesId, String name) throws Exception;

    Model getModel(long id) throws Exception;

    ModelObj getModelObj(long id) throws Exception;

    void createSpare(long id, String name) throws Exception;

    void linkSpare2ModelVariant(long spareId, long modelVariantId) throws Exception;

}
