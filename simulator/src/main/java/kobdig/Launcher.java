package kobdig;

import bogota.eventbus.EventRessource;
import bogota.eventbus.EventTypes;
import kobdig.urbanSimulation.EntitiesCreator;
import kobdig.service.SimulationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import reactor.Environment;
import reactor.bus.Event;
import reactor.bus.EventBus;
import reactor.core.dispatch.SynchronousDispatcher;
import reactor.fn.Consumer;

import static reactor.bus.selector.Selectors.$;

@Configuration
@EnableAutoConfiguration
@ComponentScan
@SpringBootApplication
public class Launcher implements CommandLineRunner {

    @Autowired
    public EntitiesCreator builder;

    @Autowired
    public EventBus eventBus;

    @Autowired
    public SimulationService simulation;

    @Bean
    Environment env() {
        return Environment.initializeIfEmpty()
                .assignErrorJournal();
    }

    @Bean
    EventBus createEventBus(Environment env) {
        return EventBus.create(env, SynchronousDispatcher.INSTANCE);

    }

    public static void main(String[] args){
        ApplicationContext app = SpringApplication.run(Launcher.class, args);
    }

    private void subscribe(String eventType, Consumer<Event<EventRessource>> consumer) {
        eventBus.on($(eventType), consumer);
    }

    @Override
    public void run(String... strings) throws Exception {
        subscribe(EventTypes.StateSimulatorMessage, simulation);
        subscribe(EventTypes.StopSimulatorMessage, simulation);
        subscribe(EventTypes.TabStateSimulatorMessage, simulation);
    }
}
