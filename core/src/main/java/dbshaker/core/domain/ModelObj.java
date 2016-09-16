package dbshaker.core.domain;

import java.util.Collection;

/**
 *
 */
public interface ModelObj {

    long getId();

    SeriesObj getModel();

    String getName();

    Collection<SpareObj> getSpares();
}
