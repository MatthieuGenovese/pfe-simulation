package kobdig.controller;

import kobdig.event.EventRessource;
import kobdig.event.SimulationMessage;
import kobdig.urbanSimulation.EntitiesCreator;
import kobdig.urbanSimulation.Simulation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class WebController {

    @Autowired
    EntitiesCreator entitiesCreator;

    @Autowired
    Simulation simulation;

    @RequestMapping(value = "/simulation", method = RequestMethod.POST)
    public ResponseEntity changeTrafficLight(@RequestBody EventRessource<SimulationMessage> simulationMessageEventRessource) throws InterruptedException {

        entitiesCreator.setNbrPromoter(simulationMessageEventRessource.getValue().getNbrPromoter());
        entitiesCreator.setNbrHousehold(simulationMessageEventRessource.getValue().getNbrHousehold());
        entitiesCreator.setNbrInvestor(simulationMessageEventRessource.getValue().getNbrInvestor());
        entitiesCreator.setNumSim(simulationMessageEventRessource.getValue().getNum());
        simulation.start();
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
