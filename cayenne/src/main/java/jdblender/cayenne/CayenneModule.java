package jdblender.cayenne;

import org.apache.cayenne.configuration.Constants;
import org.apache.cayenne.di.Binder;
import org.apache.cayenne.di.Module;

public class CayenneModule implements Module {

    /**
     * THIS one will turn off objects synchroniztion on update.
     *
     * @see https://cayenne.apache.org/docs/4.0/cayenne-guide/performance-tuning.html#turning-off-synchronization-of-objectcontexts
     */
    @Override
    public void configure(Binder binder) {
        binder.bindMap(Constants.PROPERTIES_MAP).put(Constants.SERVER_CONTEXTS_SYNC_PROPERTY, "false");
    }
}
