package kobdig.controller;

import bogota.eventbus.EventRessource;
import bogota.eventbus.EventTypes;
import bogota.eventbus.input.*;
import org.springframework.aop.target.LazyInitTargetSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;
import reactor.bus.Event;
import reactor.bus.EventBus;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
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

    @Autowired
    EventBus eventBus;

    private List<SimulationMessage> simulationMessages = new ArrayList<>();

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
        eventBus.notify(EventTypes.TabStateSimulatorMessage, Event.wrap(stateEventRessource));
        simulationMessages.clear();
        return myInterface;
    }

    @PostMapping("/state")
    public ResponseEntity<Void> startSimulation(@RequestBody EventRessource<SimulationMessage> stateEventRessource) {

        System.out.println("Send message to simulator");
        eventBus.notify(EventTypes.StateSimulatorMessage, Event.wrap(stateEventRessource));
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/extract")
    public ResponseEntity<Void> extractData(@RequestBody EventRessource<ExtractDataMessage> extractDataMessageEventRessource){
        System.out.println("Send message to simulator");
        eventBus.notify(EventTypes.ExtractDataMessage, Event.wrap(extractDataMessageEventRessource));
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/remove")
    public ResponseEntity<Void> deleteSimulation(@RequestBody EventRessource<RemoveSimulationMessage> removeSimulationMessageEventRessource){
        System.out.println("Send message to simulator");
        eventBus.notify(EventTypes.RemoveSimulationMessage, Event.wrap(removeSimulationMessageEventRessource));
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/statetab")
    public ResponseEntity<Void> startTabSimulation(@RequestBody EventRessource<TabSimulationMessage> stateEventRessource) {

        System.out.println("Send message to simulator");
        eventBus.notify(EventTypes.TabStateSimulatorMessage, Event.wrap(stateEventRessource));
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/stop")
    public ResponseEntity<Void> stopSimulation(@RequestBody EventRessource<StopSimulationMessage> stateEventRessource) {

        System.out.println("Send message to simulator");
        eventBus.notify(EventTypes.StopSimulatorMessage, Event.wrap(stateEventRessource));
        return new ResponseEntity<>(HttpStatus.OK);
    }
}