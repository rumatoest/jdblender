package dbshaker.jdbc;

import dbshaker.core.ShakerApp;

public class AppJdbc {

    public static void main(String[] args) throws Exception {
        ShakerApp app = new ShakerApp();
        app.main(new RunnerJdbc(), args);
    }
}
