package jdblender.core;

import jdblender.core.domain.Brand;
import jdblender.core.domain.Model;
import jdblender.core.domain.ModelObj;
import jdblender.core.domain.Series;
import jdblender.core.domain.SeriesObj;
import jdblender.core.domain.Spare;
import jdblender.core.domain.SpareObj;

import java.util.Collection;

/**
 * Will run code exactly on particular DB framework.
 *
 * @author rumatoest
 */
public interface FrameworkRunner {
    
    /**
     * @return By default return 1
     */
    int getFactor();

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

    /**
     * Model object with all spares.
     */
    ModelObj getModelObjWithSpares(long id) throws Exception;

    void createSpare(long id, long brandId, String name, String label, boolean flag, int num) throws Exception;

    Spare getSpare(long id) throws Exception;

    /**
     * Any param can be null in this case we do not need any SQL condition.
     *
     * @param numFromInclusive Use exact match if next param is null
     * @param numToInclusive Skip if previous param null
     */
    Collection<? extends Spare> getSpares(String label, Boolean flag, Integer numFromInclusive, Integer numToInclusive) throws Exception;

    SpareObj getSpareObj(long id) throws Exception;

    void linkModel2Spare(long modelId, long spareId) throws Exception;

    /**
     * For slow framework, otherwise just use same logic as in linkModel2Spare
     */
    void linkModel2SpareOptimized(long modelId, long spareId) throws Exception;
}
