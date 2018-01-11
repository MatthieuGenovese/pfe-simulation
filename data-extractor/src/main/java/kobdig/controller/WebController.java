package kobdig.controller;

import kobdig.eventbus.EventRessource;
import kobdig.eventbus.EventTypes;
import kobdig.eventbus.input.ExtractDataMessage;
import kobdig.service.DataExtractor;
import kobdig.mongo.collections.ConfigurationMongo;
import kobdig.mongo.repository.*;
import kobdig.sql.repository.PropertyRepository;
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

    @Autowired
    public DataExtractor extractor;


    @PostMapping("/extract")
    public ResponseEntity<Void> extractData(@RequestBody EventRessource<ExtractDataMessage> message){
        switch(message.getValue().getEntity()){
            case "household":
                extractor.findHouseholdsBySimulationId(message.getValue().getIdSimulation());
                break;
            case "promoter":
                extractor.findPromotersBySimulationId(message.getValue().getIdSimulation());
                break;
            case "investor":
                extractor.findInvestorsBySimulationId(message.getValue().getIdSimulation());
                break;
            case "land":
                extractor.findLandsBySimulationId(message.getValue().getIdSimulation());
                break;
            case "property":
                extractor.findPropertiesBySimulationId(message.getValue().getIdSimulation());
                break;
            case "configuration":
                extractor.findConfigurationBySimulationId(message.getValue().getIdSimulation());
                break;
            default:
                break;
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

}