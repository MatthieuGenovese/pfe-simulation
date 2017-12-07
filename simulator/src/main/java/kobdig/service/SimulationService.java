package kobdig.service;

import bogota.eventbus.EventRessource;
import bogota.eventbus.EventTypes;
import bogota.eventbus.input.SimulationMessage;
import bogota.eventbus.input.TabSimulationMessage;
import kobdig.access.repository.PropertyRepository;
import kobdig.access.repository.SauvegardeRepository;
import kobdig.access.tables.PropertyE;
import kobdig.access.tables.Sauvegarde;
import kobdig.urbanSimulation.EntitiesCreator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Service;
import reactor.bus.Event;
import reactor.fn.Consumer;

import java.util.List;


@Service
public class SimulationService implements Consumer<Event<EventRessource>> {

    @Autowired
    EntitiesCreator entitiesCreator;

    @Autowired
    Simulation simulation;

    @Autowired
    SauvegardeRepository sauvegardeRepository;

    @Autowired
    PropertyRepository propertyRepository;

    @Override
    public void accept(Event<EventRessource> eventRessourceEvent) {
        switch (eventRessourceEvent.getData().getType()) {
            case EventTypes.StateSimulatorMessage:
                int idSimulation = 0;
                for(PropertyE propertyE : propertyRepository.findAll()){
                    idSimulation = propertyE.getId();
                }
                idSimulation++;
                EventRessource<SimulationMessage> messageRessource =
                        (EventRessource<SimulationMessage>) eventRessourceEvent.getData();

                SimulationMessage message = messageRessource.getValue();
                sauvegardeRepository.save(new Sauvegarde(message.getNum(), message.getNbrHousehold(), message.getNbrPromoter(), message.getNbrInvestor(), idSimulation));

                entitiesCreator.setNumSim(message.getNum());
                entitiesCreator.setNbrInvestor(message.getNbrInvestor());
                entitiesCreator.setNbrPromoter(message.getNbrPromoter());
                entitiesCreator.setNbrHousehold(message.getNbrHousehold());
                entitiesCreator.setId(idSimulation);
                entitiesCreator.createAll();
                simulation.start();

                break;
            case EventTypes.TabStateSimulatorMessage:

                EventRessource<TabSimulationMessage> tabMessageRessource =
                        (EventRessource<TabSimulationMessage>) eventRessourceEvent.getData();

                TabSimulationMessage tabMessage = tabMessageRessource.getValue();

                for(SimulationMessage simulationMessage : tabMessage.getSimulationMessageList()){
                    int idSimulationBis = 0;
                    for(PropertyE propertyE : propertyRepository.findAll()){
                        idSimulationBis = propertyE.getId();
                    }
                    idSimulationBis++;
                    sauvegardeRepository.save(new Sauvegarde(simulationMessage.getNum(), simulationMessage.getNbrHousehold(), simulationMessage.getNbrPromoter(), simulationMessage.getNbrInvestor(), idSimulationBis));

                    entitiesCreator.setNumSim(simulationMessage.getNum());
                    entitiesCreator.setNbrInvestor(simulationMessage.getNbrInvestor());
                    entitiesCreator.setNbrPromoter(simulationMessage.getNbrPromoter());
                    entitiesCreator.setNbrHousehold(simulationMessage.getNbrHousehold());
                    entitiesCreator.setId(idSimulationBis);
                    entitiesCreator.createAll();
                    simulation.start();

                    while(simulation.isRunning()){
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
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
