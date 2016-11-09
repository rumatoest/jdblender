package jdblender.sjdbc;

import jdblender.core.BlenderApp;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class AppSpringJdbc {

    public static void main(String[] args) throws Exception {
        AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext(Config.class);
        BlenderApp app = new BlenderApp();
        app.main(ctx.getBean(RunnerSpringJdbc.class), args);
    }
}
