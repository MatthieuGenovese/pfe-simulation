package kobdig.controller;

import kobdig.event.EventRessource;
import kobdig.event.EventTypes;
import kobdig.event.input.SimulationMessage;
import kobdig.urbanSimulation.service.Sauvegarde;
import kobdig.urbanSimulation.service.SaveService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import reactor.bus.Event;
import reactor.bus.EventBus;

@RestController
public class WebController {

    @Autowired
    EventBus eventBus;

    @Autowired
    SaveService saveService;

    @GetMapping("/interface")
    public String view() {
        return "interface";
    }

    @GetMapping("/sauvegarde")
    public String sauvegarde() {
        String res = "";

        for(Sauvegarde sauvegarde : saveService.retrieveAllSauvegarde()){
            res = res + sauvegarde.toString() + "</br>";
        }

        return res;
    }

    @PostMapping("/state")
    public ResponseEntity<Void> startSimulation(@RequestBody EventRessource<SimulationMessage> stateEventRessource) {

        System.out.println("Send message to simulator");
        eventBus.notify(EventTypes.StateSimulatorMessage, Event.wrap(stateEventRessource));
        return new ResponseEntity<>(HttpStatus.OK);
    }
}