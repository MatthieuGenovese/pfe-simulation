package kobdig.urbanSimulation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class Launcher {

    @Autowired
    EntitiesCreator builder;

    public static void main(String[] args){
        ConfigurableApplicationContext ctx = SpringApplication.run(Launcher.class, args);
        Launcher launcher = ctx.getBean(Launcher.class);
    }
}
