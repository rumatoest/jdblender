package dbshaker.jdbc;

import dbshaker.core.ShakerApp;

public class App {

    public static void main(String[] args) throws Exception {
        ShakerApp app = new ShakerApp();
        app.main(new Runner(), args);
    }
}
