package jdblender.jdbc;

import jdblender.core.BlenderApp;

public class AppJdbc {

    public static void main(String[] args) throws Exception {
        BlenderApp app = new BlenderApp();
        app.main(new RunnerJdbc(), args);
    }
}
