package jdblender.hibernate;

import jdblender.core.BlenderApp;

public class AppHibernate {

    public static void main(String[] args) throws Exception {
        BlenderApp app = new BlenderApp();
        app.main(new RunnerHibernate(), args);
    }
}
