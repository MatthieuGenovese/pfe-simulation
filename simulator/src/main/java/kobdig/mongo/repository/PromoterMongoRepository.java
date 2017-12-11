package kobdig.mongo.repository;

import kobdig.mongo.collections.InvestorMongo;
import kobdig.mongo.collections.PromoterMongo;
import kobdig.urbanSimulation.entities.agents.Promoter;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

/**
 * Created by Matthieu on 08/12/2017.
 */
public interface PromoterMongoRepository  extends MongoRepository<PromoterMongo, Integer> {

    public List<PromoterMongo> findByidSimulation(int idSimulation);

}
