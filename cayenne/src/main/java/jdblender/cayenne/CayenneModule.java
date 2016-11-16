package jdblender.cayenne;

import org.apache.cayenne.configuration.Constants;
import org.apache.cayenne.di.Binder;
import org.apache.cayenne.di.Module;

public class CayenneModule implements Module {

    @Override
    public void configure(Binder binder) {
        binder.bindMap(Constants.PROPERTIES_MAP).put(Constants.SERVER_CONTEXTS_SYNC_PROPERTY, "false");
    }
}
