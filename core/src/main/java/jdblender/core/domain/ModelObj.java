package jdblender.core.domain;

import java.util.Collection;
import java.util.Set;

/**
 *
 */
public interface ModelObj {

    long getId();

    SeriesObj getSeries();

    String getName();

    Set<? extends SpareObj> getSpares();
}
