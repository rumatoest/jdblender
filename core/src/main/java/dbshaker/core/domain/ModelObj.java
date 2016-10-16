package dbshaker.core.domain;

import java.util.Collection;

/**
 *
 */
public interface ModelObj {

    long getId();

    SeriesObj getSeries();

    String getName();

    Collection<SpareObj> getSpares();
}
