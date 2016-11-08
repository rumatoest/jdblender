package jdblender.jdbc;

import jdblender.core.ShakerApp;

public class AppJdbc {

    public static void main(String[] args) throws Exception {
        ShakerApp app = new ShakerApp();
        app.main(new RunnerJdbc(), args);
    }
}
