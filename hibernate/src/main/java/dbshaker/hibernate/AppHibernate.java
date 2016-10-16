package dbshaker.hibernate;

import dbshaker.core.ShakerApp;

public class AppHibernate {

    public static void main(String[] args) throws Exception {
        ShakerApp app = new ShakerApp();
        app.main(new RunnerHibernate(), args);
    }
}
