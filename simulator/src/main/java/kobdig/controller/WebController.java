package kobdig.controller;

import bogota.eventbus.EventRessource;
import bogota.eventbus.EventTypes;
import bogota.eventbus.input.SimulationMessage;
import kobdig.sauvegarde.Sauvegarde;
import kobdig.sauvegarde.SauvegardeRepository;
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
    SauvegardeRepository sauvegardeRepository;

    @GetMapping("/interface")
    public String view() {
        return "interface";
    }


    @PostMapping("/state")
    public ResponseEntity<Void> startSimulation(@RequestBody EventRessource<SimulationMessage> stateEventRessource) {

        System.out.println("Send message to simulator");
        sauvegardeRepository.save(new Sauvegarde(30, 50, 50, 50, 5));
        eventBus.notify(EventTypes.StateSimulatorMessage, Event.wrap(stateEventRessource));
        return new ResponseEntity<>(HttpStatus.OK);
    }
}