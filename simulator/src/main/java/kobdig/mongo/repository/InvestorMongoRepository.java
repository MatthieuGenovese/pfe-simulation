package kobdig.mongo.repository;

import kobdig.mongo.collections.InvestorMongo;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * Created by Matthieu on 08/12/2017.
 */
public interface InvestorMongoRepository extends MongoRepository<InvestorMongo, Integer> {

}