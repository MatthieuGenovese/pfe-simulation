package kobdig.controller;

import bogota.eventbus.EventRessource;
import bogota.eventbus.EventTypes;
import bogota.eventbus.input.*;
import kobdig.service.DataExtractor;
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

    private String myInterface = "<div><form method=\"post\" action=\"results\">\n" +
            "    <p>\n" +
            "        <label>Number of steps</label> : <input type=\"text\" name=\"num\" />\n" +
            "    </p>\n" +
            "    <p>\n" +
            "        <label>Number of households</label> : <input type=\"text\" name=\"nbrHousehold\" />\n" +
            "    </p>\n" +
            "    <p>\n" +
            "        <label>Number of investors</label> : <input type=\"text\" name=\"nbrInvestor\" />\n" +
            "    </p>\n" +
            "    <p>\n" +
            "        <label>Number of promoters</label> : <input type=\"text\" name=\"nbrPromoter\" />\n" +
            "    </p>\n" +
            "    <p>\n" +
            "        <label>List of transports</label> : <input type=\"text\" name=\"listOfTransport\" />\n" +
            "    </p>\n" +
            "    <p>\n" +
            "        <label>List of equipments</label> : <input type=\"text\" name=\"listOfEquipment\" />\n" +
            "    </p>\n" +
            "    <p>\n" +
            "        <label>File of households</label> : <input type=\"text\" name=\"fileHousehold\" />\n" +
            "    </p>\n" +
            "    <p>\n" +
            "        <label>File of investors</label> : <input type=\"text\" name=\"fileInvestor\" />\n" +
            "    </p>\n" +
            "    <p>\n" +
            "        <label>File of promoters</label> : <input type=\"text\" name=\"filePromoter\" />\n" +
            "    </p>\n" +
            "    <p>\n" +
            "        <input type=\"submit\" value=\"Valider\" />\n" +
            "    </p>\n" +
            "</form></div>";

    /*Simulation*/
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

    /*extractor*/
    @Autowired
    public DataExtractor extractor;

    @Autowired
    public PropertyMongoRepository propertyMongoRepository;

    @Autowired
    public HouseholdMongoRepository householdMongoRepository;

    @Autowired
    public PromoterMongoRepository promoterMongoRepository;

    @Autowired
    public LandMongoRepository landMongoRepository;

    @Autowired
    public InvestorMongoRepository investorMongoRepository;


    @GetMapping("/interface")
    public String showInterface(){
        return myInterface;
    }

    @PostMapping("/results")
    public String showResults(WebRequest request) {
        String nbrOfHousehold  = request.getParameter("nbrHousehold");
        String nbrOfInvestor  = request.getParameter("nbrInvestor");
        String nbrOfPromoter  = request.getParameter("nbrPromoter");

        int nbrHousehold = Integer.parseInt(nbrOfHousehold);
        int nbrInvestor = Integer.parseInt(nbrOfInvestor);
        int nbrPromoter = Integer.parseInt(nbrOfPromoter);

        String num = request.getParameter("num");

        int step = Integer.parseInt(num);

        String listOfTransports = request.getParameter("listOfTransport");
        String listOfEquipments = request.getParameter("listOfEquipment");

        String[] listT = listOfTransports.split(",");
        String[] listE = listOfEquipments.split(",");

        List<Integer> listTransports = new ArrayList<>();
        List<Integer> listEquipments =new ArrayList<>();

        for(String s : listT){
            listTransports.add(Integer.parseInt(s));
        }

        for(String s : listE){
            listEquipments.add(Integer.parseInt(s));
        }

        String fileOfHousehold  = request.getParameter("fileHousehold");
        String fileOfInvestor  = request.getParameter("fileInvestor");
        String fileOfPromoter  = request.getParameter("filePromoter");

        SimulationMessage simulationMessage = new SimulationMessage();

        simulationMessage.setNum(step);
        simulationMessage.setNbrHousehold(nbrHousehold);
        simulationMessage.setNbrInvestor(nbrInvestor);
        simulationMessage.setNbrPromoter(nbrPromoter);
        simulationMessage.setListOfEquipment(listEquipments);
        simulationMessage.setListOfTransport(listTransports);
        simulationMessage.setFileHousehold(fileOfHousehold);
        simulationMessage.setFileInvestor(fileOfInvestor);
        simulationMessage.setFilePromoter(fileOfPromoter);

        simulationMessages.add(simulationMessage);

        return myInterface+"<div>"+listToString()+"</div>"+"<form method=\"post\" action=\"start\">\n" +
                "<input type=\"submit\" value=\"Envoyer\" /></form>";
    }

    public String listToString(){
        String res = "";
        for(SimulationMessage simulationMessage : simulationMessages){
            res = res + "_____________________________________________" + "<br>";
            res = res + "Number of step: " + simulationMessage.getNum() + "<br>";
            res = res + "Number of household: " + simulationMessage.getNbrHousehold() + "<br>";
            res = res + "Number of investor: " + simulationMessage.getNbrInvestor() + "<br>";
            res = res + "Number of promoter: " + simulationMessage.getNbrPromoter() + "<br>";
            res = res + "List of transports: " + simulationMessage.getListOfTransport() + "<br>";
            res = res + "List of equipments: " + simulationMessage.getListOfEquipment() + "<br>";
            res = res + "File of household: " + simulationMessage.getFileHousehold() + "<br>";
            res = res + "File of investor: " + simulationMessage.getFileInvestor() + "<br>";
            res = res + "File of promoter: " + simulationMessage.getFilePromoter() + "<br>";
            res = res + "_____________________________________________" + "<br>";

        }
        return res;
    }

    @PostMapping("/start")
    public String start(){
        EventRessource<TabSimulationMessage> stateEventRessource = new EventRessource<>();
        stateEventRessource.setType(EventTypes.TabStateSimulatorMessage);
        TabSimulationMessage tabSimulationMessage = new TabSimulationMessage();
        tabSimulationMessage.setSimulationMessageList(simulationMessages);
        stateEventRessource.setValue(tabSimulationMessage);

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

        simulationMessages.clear();
        return myInterface;
    }

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

    @PostMapping("/extract")
    public ResponseEntity<Void> extractData(@RequestBody EventRessource<ExtractDataMessage> message){
        switch(message.getValue().getEntity()){
            case "household":
                extractor.findHouseholdsBySimulationId(householdMongoRepository, message.getValue().getIdSimulation());
                break;
            case "promoter":
                extractor.findPromotersBySimulationId(promoterMongoRepository, message.getValue().getIdSimulation());
                break;
            case "investor":
                extractor.findInvestorsBySimulationId(investorMongoRepository, message.getValue().getIdSimulation());
                break;
            case "land":
                extractor.findLandsBySimulationId(landMongoRepository, message.getValue().getIdSimulation());
                break;
            case "property":
                extractor.findPropertiesBySimulationId(propertyMongoRepository, message.getValue().getIdSimulation());
                break;
            case "configuration":
                extractor.findConfigurationBySimulationId(configurationMongoRepository, message.getValue().getIdSimulation());
                break;
            default:
                break;
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