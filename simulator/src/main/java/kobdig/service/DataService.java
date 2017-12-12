package kobdig.service;

import bogota.eventbus.EventRessource;
import bogota.eventbus.EventTypes;
import bogota.eventbus.input.ExtractDataMessage;
import kobdig.mongo.access.DataExtractor;
import kobdig.mongo.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.bus.Event;
import reactor.fn.Consumer;

/**
 * Created by Matthieu on 11/12/2017.
 */

@Service
public class DataService implements Consumer<Event<EventRessource>> {
    @Autowired
    DataExtractor extractor;

    @Autowired
    PropertyMongoRepository propertyMongoRepository;

    @Autowired
    HouseholdMongoRepository householdMongoRepository;

    @Autowired
    PromoterMongoRepository promoterMongoRepository;

    @Autowired
    LandMongoRepository landMongoRepository;

    @Autowired
    InvestorMongoRepository investorMongoRepository;


    @Override
    public void accept(Event<EventRessource> eventRessourceEvent) {
        switch(eventRessourceEvent.getData().getType()){
            case EventTypes.ExtractDataMessage:
                EventRessource<ExtractDataMessage> message = (EventRessource<ExtractDataMessage>) eventRessourceEvent.getData();
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
                }
                break;
        }
    }
}
