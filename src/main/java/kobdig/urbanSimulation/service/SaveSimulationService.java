package kobdig.urbanSimulation.service;


import kobdig.event.EventRessource;
import kobdig.event.EventTypes;
import kobdig.event.input.SimulationMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.bus.Event;
import reactor.fn.Consumer;

@Service
public class SaveSimulationService implements Consumer<Event<EventRessource>> {

    @Autowired
    SaveService saveService;

    @Override
    public void accept(Event<EventRessource> eventRessourceEvent) {
        switch (eventRessourceEvent.getData().getType()) {
            case EventTypes.StateSimulatorMessage:
                System.out.println("recu");
                EventRessource<SimulationMessage> messageRessource =
                        (EventRessource<SimulationMessage>) eventRessourceEvent.getData();

                SimulationMessage message = messageRessource.getValue();
                Sauvegarde sauvegarde = new Sauvegarde(message.getNum(), message.getNbrHousehold(), message.getNbrInvestor(), message.getNbrPromoter(), message.getId());
                saveService.add(sauvegarde);
                break;
            default:
                break;
        }
    }
}