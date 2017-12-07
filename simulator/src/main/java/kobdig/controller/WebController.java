package kobdig.controller;

import bogota.eventbus.EventRessource;
import bogota.eventbus.EventTypes;
import bogota.eventbus.input.SimulationMessage;
import bogota.eventbus.input.StopSimulationMessage;
import bogota.eventbus.input.TabSimulationMessage;
import kobdig.access.repository.*;
import kobdig.access.tables.DivisionE;
import kobdig.access.tables.EquipmentE;
import kobdig.access.tables.Sauvegarde;
import kobdig.access.tables.TransportNetworkE;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import reactor.bus.Event;
import reactor.bus.EventBus;

@RestController
public class WebController {

    @Autowired
    EventBus eventBus;

    @PostMapping("/state")
    public ResponseEntity<Void> startSimulation(@RequestBody EventRessource<SimulationMessage> stateEventRessource) {

        System.out.println("Send message to simulator");
        eventBus.notify(EventTypes.StateSimulatorMessage, Event.wrap(stateEventRessource));
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