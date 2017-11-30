package kobdig.service;

import bogota.eventbus.EventRessource;
import bogota.eventbus.EventTypes;
import bogota.eventbus.input.SimulationMessage;
import kobdig.urbanSimulation.EntitiesCreator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Service;
import reactor.bus.Event;
import reactor.fn.Consumer;


@Service
public class SimulationService implements Consumer<Event<EventRessource>> {

    @Autowired
    EntitiesCreator entitiesCreator;

    @Autowired
    Simulation simulation;

    @Override
    public void accept(Event<EventRessource> eventRessourceEvent) {
        switch (eventRessourceEvent.getData().getType()) {
            case EventTypes.StateSimulatorMessage:
                System.out.println("salut");
                EventRessource<SimulationMessage> messageRessource =
                        (EventRessource<SimulationMessage>) eventRessourceEvent.getData();

                SimulationMessage message = messageRessource.getValue();

                entitiesCreator.setNumSim(message.getNum());
                entitiesCreator.setNbrInvestor(message.getNbrInvestor());
                entitiesCreator.setNbrPromoter(message.getNbrPromoter());
                entitiesCreator.setNbrHousehold(message.getNbrHousehold());
                entitiesCreator.setId(message.getId());
                entitiesCreator.createAll();
                simulation.start();

                break;
            case EventTypes.StopSimulatorMessage:
                simulation.stop();
                break;
            default:
                break;
        }
    }
}
