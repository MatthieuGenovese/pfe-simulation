package kobdig.service;

import bogota.eventbus.EventRessource;
import bogota.eventbus.EventTypes;
import bogota.eventbus.input.SimulationMessage;
import bogota.eventbus.input.TabSimulationMessage;
import kobdig.sql.repository.PropertyRepository;
import kobdig.mongo.collections.ConfigurationMongo;
import kobdig.mongo.repository.ConfigurationMongoRepository;
import kobdig.urbanSimulation.EntitiesCreator;
import kobdig.urbanSimulation.utils.SimulationLogging;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Service;
import reactor.bus.Event;
import reactor.fn.Consumer;

import java.text.DateFormat;
import java.util.Date;


@Service
@ComponentScan()
public class SimulationService implements Consumer<Event<EventRessource>> {

    @Autowired
    EntitiesCreator entitiesCreator;

    @Autowired
    Simulation simulation;

    @Autowired
    PropertyRepository propertyRepository;

    @Autowired
    ConfigurationMongoRepository configurationMongoRepository;

    @Autowired
    SimulationLogging log;

    @Override
    public void accept(Event<EventRessource> eventRessourceEvent) {

        Date time = new Date();
        DateFormat shortDateFormat = DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.YEAR_FIELD);
        String date = shortDateFormat.format(time);
        switch (eventRessourceEvent.getData().getType()) {
            case EventTypes.StateSimulatorMessage:
                int idSimulation = 0;
                while(configurationMongoRepository.findByidSimulation(idSimulation) != null){
                    idSimulation++;
                }
                EventRessource<SimulationMessage> messageRessource =
                        (EventRessource<SimulationMessage>) eventRessourceEvent.getData();
                SimulationMessage message = messageRessource.getValue();
                if(!simulation.isRunning()) {
                    entitiesCreator.setNumSim(message.getNum());
                    entitiesCreator.setNbrInvestor(message.getNbrInvestor());
                    entitiesCreator.setNbrPromoter(message.getNbrPromoter());
                    entitiesCreator.setNbrHousehold(message.getNbrHousehold());
                    entitiesCreator.setId(idSimulation);
                    entitiesCreator.setListOfEquipment(message.getListOfEquipment());
                    entitiesCreator.setListOfTransport(message.getListOfTransport());
                    entitiesCreator.setFileHousehold(message.getFileHousehold());
                    entitiesCreator.setFileInvestor(message.getFileInvestor());
                    entitiesCreator.setFilePromoter(message.getFilePromoter());
                    entitiesCreator.createAll();
                    configurationMongoRepository.save(new ConfigurationMongo(date, message.getNum(), message.getNbrHousehold(), message.getNbrPromoter(), message.getNbrInvestor(), idSimulation, message.getFileHousehold(), message.getFileInvestor(), message.getFilePromoter(), message.getListOfEquipment(), message.getListOfTransport()));
                    simulation.start();
                    while(simulation.isRunning()){
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    Date time2 = new Date();
                    log.writeData("------------------------------------------------------------------");
                    log.writeData("SIMULATION DU " + date + " NUMERO " + idSimulation);
                    log.writeData("terminée à " +shortDateFormat.format(time2));
                    log.writeData("------------------------------------------------------------------");
                }

                break;
            case EventTypes.TabStateSimulatorMessage:

                EventRessource<TabSimulationMessage> tabMessageRessource =
                        (EventRessource<TabSimulationMessage>) eventRessourceEvent.getData();

                TabSimulationMessage tabMessage = tabMessageRessource.getValue();

                for(SimulationMessage simulationMessage : tabMessage.getSimulationMessageList()){
                    int idSimulationBis = 0;
                    while(configurationMongoRepository.findByidSimulation(idSimulationBis) != null){
                        idSimulationBis++;
                    }
                    entitiesCreator.setNumSim(simulationMessage.getNum());
                    entitiesCreator.setNbrInvestor(simulationMessage.getNbrInvestor());
                    entitiesCreator.setNbrPromoter(simulationMessage.getNbrPromoter());
                    entitiesCreator.setNbrHousehold(simulationMessage.getNbrHousehold());
                    entitiesCreator.setId(idSimulationBis);
                    entitiesCreator.setListOfEquipment(simulationMessage.getListOfEquipment());
                    entitiesCreator.setListOfTransport(simulationMessage.getListOfTransport());
                    entitiesCreator.setFileHousehold(simulationMessage.getFileHousehold());
                    entitiesCreator.setFileInvestor(simulationMessage.getFileInvestor());
                    entitiesCreator.setFilePromoter(simulationMessage.getFilePromoter());
                    entitiesCreator.createAll();
                    configurationMongoRepository.save(new ConfigurationMongo(date, simulationMessage.getNum(), simulationMessage.getNbrHousehold(), simulationMessage.getNbrPromoter(), simulationMessage.getNbrInvestor(), idSimulationBis, simulationMessage.getFileHousehold(), simulationMessage.getFileInvestor(), simulationMessage.getFilePromoter(), simulationMessage.getListOfEquipment(), simulationMessage.getListOfTransport()));
                    simulation.start();
                    while(simulation.isRunning()){
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    Date time2 = new Date();
                    log.writeData("------------------------------------------------------------------");
                    log.writeData("SIMULATION DU " + date + " NUMERO " + idSimulationBis);
                    log.writeData("terminée à " +shortDateFormat.format(time2));
                    log.writeData("------------------------------------------------------------------");
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
