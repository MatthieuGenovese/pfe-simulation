package kobdig.service;

import bogota.eventbus.EventRessource;
import bogota.eventbus.EventTypes;
import bogota.eventbus.input.SimulationMessage;
import bogota.eventbus.input.TabSimulationMessage;
import kobdig.access.repository.SauvegardeRepository;
import kobdig.access.tables.Sauvegarde;
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

    @Autowired
    SauvegardeRepository sauvegardeRepository;

    @Override
    public void accept(Event<EventRessource> eventRessourceEvent) {
        switch (eventRessourceEvent.getData().getType()) {
            case EventTypes.StateSimulatorMessage:

                EventRessource<SimulationMessage> messageRessource =
                        (EventRessource<SimulationMessage>) eventRessourceEvent.getData();

                SimulationMessage message = messageRessource.getValue();
                sauvegardeRepository.save(new Sauvegarde(message.getNum(), message.getNbrHousehold(), message.getNbrPromoter(), message.getNbrInvestor(), message.getId()));

                entitiesCreator.setNumSim(message.getNum());
                entitiesCreator.setNbrInvestor(message.getNbrInvestor());
                entitiesCreator.setNbrPromoter(message.getNbrPromoter());
                entitiesCreator.setNbrHousehold(message.getNbrHousehold());
                entitiesCreator.setId(message.getId());
                entitiesCreator.createAll();
                simulation.start();

                break;
            case EventTypes.TabStateSimulatorMessage:
                EventRessource<TabSimulationMessage> tabMessageRessource =
                        (EventRessource<TabSimulationMessage>) eventRessourceEvent.getData();

                TabSimulationMessage tabMessage = tabMessageRessource.getValue();
                int i = 0;
                while(i < tabMessage.getSimulationMessageList().size()){
                    i++;
                    sauvegardeRepository.save(new Sauvegarde(tabMessage.getSimulationMessageList().get(i).getNum(), tabMessage.getSimulationMessageList().get(i).getNbrHousehold(), tabMessage.getSimulationMessageList().get(i).getNbrPromoter(), tabMessage.getSimulationMessageList().get(i).getNbrInvestor(), tabMessage.getSimulationMessageList().get(i).getId()));

                    entitiesCreator.setNumSim(tabMessage.getSimulationMessageList().get(i).getNum());
                    entitiesCreator.setNbrInvestor(tabMessage.getSimulationMessageList().get(i).getNbrInvestor());
                    entitiesCreator.setNbrPromoter(tabMessage.getSimulationMessageList().get(i).getNbrPromoter());
                    entitiesCreator.setNbrHousehold(tabMessage.getSimulationMessageList().get(i).getNbrHousehold());
                    entitiesCreator.setId(tabMessage.getSimulationMessageList().get(i).getId());
                    entitiesCreator.createAll();
                    simulation.start();

                    try {
                        Thread.sleep(30000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

                break;
            case EventTypes.StopSimulatorMessage:
                simulation.stop();
                break;
            default:
                break;
        }
    }
}
