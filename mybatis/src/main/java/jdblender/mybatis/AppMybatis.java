package jdblender.mybatis;

import jdblender.core.BlenderApp;

public class AppMybatis {
    public static void main(String[] args) throws Exception {
        BlenderApp app = new BlenderApp();
        app.main(new RunnerMybatis(), args);
    }
}
