package kobdig;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import static reactor.bus.selector.Selectors.$;

@Configuration
@EnableAutoConfiguration
@ComponentScan
@SpringBootApplication
public class Launcher {


    public static void main(String[] args){
        SpringApplication.run(Launcher.class, args);
    }


}
