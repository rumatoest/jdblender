package dbshaker.eclipselink;

import dbshaker.core.ShakerApp;

public class AppEclipse {

    public static void main(String[] args) throws Exception {
        ShakerApp app = new ShakerApp();
        app.main(new RunnerEclipse(), args);
    }
}
