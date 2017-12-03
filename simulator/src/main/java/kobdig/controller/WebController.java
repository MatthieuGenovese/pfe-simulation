package kobdig.controller;

import bogota.eventbus.EventRessource;
import bogota.eventbus.EventTypes;
import bogota.eventbus.input.SimulationMessage;
import bogota.eventbus.input.StopSimulationMessage;
import kobdig.repository.*;
import kobdig.tables.DivisionE;
import kobdig.tables.EquipmentE;
import kobdig.tables.Sauvegarde;
import kobdig.tables.TransportNetworkE;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.bus.Event;
import reactor.bus.EventBus;

@RestController
public class WebController {

    @Autowired
    EventBus eventBus;

    @Autowired
    SauvegardeRepository sauvegardeRepository;

    @PostMapping("/state")
    public ResponseEntity<Void> startSimulation(@RequestBody EventRessource<SimulationMessage> stateEventRessource) {

        System.out.println("Send message to simulator");
        sauvegardeRepository.save(new Sauvegarde(30, 50, 50, 50, 5));
        eventBus.notify(EventTypes.StateSimulatorMessage, Event.wrap(stateEventRessource));
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/stop")
    public ResponseEntity<Void> stopSimulation(@RequestBody EventRessource<StopSimulationMessage> stateEventRessource) {

        System.out.println("Send message to simulator");
        eventBus.notify(EventTypes.StopSimulatorMessage, Event.wrap(stateEventRessource));
        return new ResponseEntity<>(HttpStatus.OK);
    }
}