package jdblender.core.domain;

import java.util.List;

/**
 *
 */
public interface ModelObj {

    long getId();

    SeriesObj getSeries();

    String getName();

    List<? extends SpareObj> getSpares();
}
