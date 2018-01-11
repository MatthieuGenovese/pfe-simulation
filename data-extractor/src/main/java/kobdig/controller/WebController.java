package kobdig.controller;

import kobdig.eventbus.input.ExtractDataMessage;
import kobdig.service.DataExtractor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class WebController {

    @Autowired
    public DataExtractor extractor;


    @PostMapping("/extract")
    public ResponseEntity<Void> extractData(@RequestBody ExtractDataMessage message){
        switch(message.getEntity()){
            case "household":
                extractor.findHouseholdsBySimulationId(message.getIdSimulation());
                break;
            case "promoter":
                extractor.findPromotersBySimulationId(message.getIdSimulation());
                break;
            case "investor":
                extractor.findInvestorsBySimulationId(message.getIdSimulation());
                break;
            case "land":
                extractor.findLandsBySimulationId(message.getIdSimulation());
                break;
            case "property":
                extractor.findPropertiesBySimulationId(message.getIdSimulation());
                break;
            case "configuration":
                extractor.findConfigurationBySimulationId(message.getIdSimulation());
                break;
            default:
                break;
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

}