package jdblender.jooq;

import jdblender.core.BlenderApp;

public class AppJooq {

    public static void main(String[] args) throws Exception {
        BlenderApp app = new BlenderApp();
        app.main(new RunnerJooq(), args);
    }
}
