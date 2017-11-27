package kobdig.controller;

import kobdig.urbanSimulation.Simulation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class WebController {

    @Autowired
    Simulation simulation;
}
