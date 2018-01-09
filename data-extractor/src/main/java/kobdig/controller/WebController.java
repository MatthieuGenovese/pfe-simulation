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

    @Autowired
    public ConfigurationMongoRepository configurationMongoRepository;

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

}