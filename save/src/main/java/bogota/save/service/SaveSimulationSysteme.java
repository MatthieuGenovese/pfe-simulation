package bogota.save.service;

import bogota.eventbus.EventRessource;
import bogota.eventbus.EventTypes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Service;
import reactor.bus.Event;
import reactor.bus.EventBus;
import reactor.fn.Consumer;

import static reactor.bus.selector.Selectors.$;

@Service
public class SaveSimulationSysteme implements CommandLineRunner {

    @Autowired
    public EventBus eventBus;

    @Autowired
    private SaveSimulationService saveSimulationService;

    @Override
    public void run(String... strings) throws Exception {
        subscribe(EventTypes.StateSimulatorMessage, saveSimulationService);
    }

    private void subscribe(String eventType, Consumer<Event<EventRessource>> consumer) {
        eventBus.on($(eventType), consumer);
    }
}
