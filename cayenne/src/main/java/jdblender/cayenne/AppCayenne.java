package jdblender.cayenne;

import jdblender.core.BlenderApp;

public class AppCayenne {
    public static void main(String[] args) throws Exception {
        BlenderApp app = new BlenderApp();
        app.main(new RunnerCayenne(), args);
    }
}
