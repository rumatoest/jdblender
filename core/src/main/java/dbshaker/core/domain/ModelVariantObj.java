package dbshaker.core.domain;

import java.util.Collection;

/**
 *
 */
public interface ModelVariantObj {

    long getId();

    ModelObj getModel();

    String getName();
    
    Collection<Spare> getSpares();
}
