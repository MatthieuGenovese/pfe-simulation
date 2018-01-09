package kobdig.controller;

import kobdig.eventbus.EventRessource;
import kobdig.eventbus.EventTypes;
import kobdig.eventbus.input.SimulationMessage;
import kobdig.eventbus.input.StopSimulationMessage;
import kobdig.eventbus.input.TabSimulationMessage;
import kobdig.mongo.collections.ConfigurationMongo;
import kobdig.mongo.repository.*;
import kobdig.service.Simulation;
import kobdig.sql.repository.PropertyRepository;
import kobdig.urbanSimulation.EntitiesCreator;
import kobdig.urbanSimulation.utils.SimulationLogging;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RestController
public class WebController {

    private List<SimulationMessage> simulationMessages = new ArrayList<>();

    @Autowired
    private EntitiesCreator entitiesCreator;

    @Autowired
    public Simulation simulation;

    @Autowired
    public PropertyRepository propertyRepository;

    @Autowired
    public ConfigurationMongoRepository configurationMongoRepository;

    @Autowired
    public SimulationLogging log;

    @PostMapping("/state")
    public ResponseEntity<Void> startSimulation(@RequestBody EventRessource<SimulationMessage> stateEventRessource) {

        Date time = new Date();
        DateFormat shortDateFormat = DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.YEAR_FIELD);
        String date = shortDateFormat.format(time);

        int idSimulation = 0;
        while(configurationMongoRepository.findByidSimulation(idSimulation) != null){
              idSimulation++;
        }
        SimulationMessage message = stateEventRessource.getValue();

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
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/statetab")
    public ResponseEntity<Void> startTabSimulation(@RequestBody EventRessource<TabSimulationMessage> stateEventRessource) {

        Date time = new Date();
        DateFormat shortDateFormat = DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.YEAR_FIELD);
        String date = shortDateFormat.format(time);

        TabSimulationMessage tabMessage = stateEventRessource.getValue();

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

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/stop")
    public ResponseEntity<Void> stopSimulation(@RequestBody EventRessource<StopSimulationMessage> stateEventRessource) {

        simulation.stop();

        return new ResponseEntity<>(HttpStatus.OK);
    }
}