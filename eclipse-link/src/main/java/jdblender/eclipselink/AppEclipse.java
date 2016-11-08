package jdblender.eclipselink;

import jdblender.core.BlenderApp;

public class AppEclipse {

    public static void main(String[] args) throws Exception {
        BlenderApp app = new BlenderApp();
        app.main(new RunnerEclipse(), args);
    }
}
