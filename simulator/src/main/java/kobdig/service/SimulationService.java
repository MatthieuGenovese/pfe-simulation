package kobdig.service;

import bogota.eventbus.EventRessource;
import bogota.eventbus.EventTypes;
import bogota.eventbus.input.SimulationMessage;
import bogota.eventbus.input.TabSimulationMessage;
import kobdig.access.sql.repository.PropertyRepository;
import kobdig.access.sql.repository.SauvegardeRepository;
import kobdig.access.sql.tables.PropertyE;
import kobdig.access.sql.tables.Sauvegarde;
import kobdig.mongo.collections.ConfigurationMongo;
import kobdig.mongo.repository.ConfigurationMongoRepository;
import kobdig.urbanSimulation.EntitiesCreator;
import kobdig.urbanSimulation.entities.agents.AbstractAgent;
import kobdig.urbanSimulation.entities.agents.Household;
import kobdig.urbanSimulation.entities.agents.Investor;
import kobdig.urbanSimulation.entities.agents.Promoter;
import kobdig.urbanSimulation.utils.SimulationLogging;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Service;
import reactor.bus.Event;
import reactor.fn.Consumer;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;


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

    @Autowired
    ConfigurationMongoRepository configurationMongoRepository;

    private SimulationLogging log = new SimulationLogging();

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
                    Household h = null;
                    Promoter p = null;
                    Investor i = null;
                    for(AbstractAgent a : entitiesCreator.getAgents()){
                        if(p != null && h != null){
                            break;
                        }
                        if(a instanceof  Household){
                            h = (Household) a;
                        }
                        else if(a instanceof Promoter){
                            p = (Promoter) a;
                        }
                    }
                    try {
                        i = entitiesCreator.getInvestors().get(0);
                    }
                    catch(IndexOutOfBoundsException e){
                        e.printStackTrace();
                    }
                    simulation.start();

                    log.writeData("------------------------------------------------------------------");
                    Date time = new Date();
                    DateFormat shortDateFormat = DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.SHORT);
                    configurationMongoRepository.save(new ConfigurationMongo(time, message.getNum(), message.getNbrHousehold(), message.getNbrPromoter(), message.getNbrInvestor(), idSimulation, h, i, p));
                    log.writeData(("SIMULATION DU " + shortDateFormat.format(time)));
                    log.writeData("simulation numéro " + idSimulation);
                    log.writeData("household " + message.getNbrHousehold());
                    log.writeData("investors " + message.getNbrInvestor() );
                    log.writeData("promoters " + message.getNbrPromoter());
                    log.writeData("CONTENU DU FICHIER HOUSEHOLD");
                    log.writeFileInput( message.getFileHousehold());
                    log.writeData("CONTENU DU FICHIER INVESTOR");
                    log.writeFileInput(message.getFileInvestor());
                    log.writeData("CONTENU DU FICHIER PROMOTER");
                    log.writeFileInput(message.getFilePromoter());
                    log.writeList("équipements utilisées ",message.getListOfEquipment());
                    log.writeList("transports utilisés ", message.getListOfTransport());
                    log.writeData("------------------------------------------------------------------");
                }

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
                    entitiesCreator.setListOfEquipment(simulationMessage.getListOfEquipment());
                    entitiesCreator.setListOfTransport(simulationMessage.getListOfTransport());
                    entitiesCreator.setFileHousehold(simulationMessage.getFileHousehold());
                    entitiesCreator.setFileInvestor(simulationMessage.getFileInvestor());
                    entitiesCreator.setFilePromoter(simulationMessage.getFilePromoter());
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
