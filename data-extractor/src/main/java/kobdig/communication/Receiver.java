package kobdig.communication;

import kobdig.service.DataExtractor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;

public class Receiver {

    @Autowired
    private DataExtractor dataExtractor;

    private static final Logger LOGGER =
            LoggerFactory.getLogger(Receiver.class);

    @KafkaListener(topics = "extract")
    public void receive(String payload) {
        LOGGER.info("received payload='{}'", payload);

        int id = Integer.parseInt(payload);

        dataExtractor.findConfigurationBySimulationId(id);
        dataExtractor.findHouseholdsBySimulationId(id);
        dataExtractor.findInvestorsBySimulationId(id);
        dataExtractor.findLandsBySimulationId(id);
        dataExtractor.findPropertiesBySimulationId(id);
        dataExtractor.findPromotersBySimulationId(id);

    }
}
