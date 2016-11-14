package jdblender.sdata;

import javax.sql.DataSource;

public class H2DS {

    private static DataSource ds;

    public static void init(DataSource ds) {
        H2DS.ds = ds;
    }

    public static DataSource get() {
        if (ds == null) {
            throw new UnknownError("No data source available");
        }
        return ds;
    }

}
