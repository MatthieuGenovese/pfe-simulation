package kobdig.acces.mongo.repository;

import kobdig.acces.mongo.collections.HouseholdMongo;
import org.springframework.data.mongodb.repository.MongoRepository;
/**
 * Created by Matthieu on 07/12/2017.
 */
public interface HouseholdMongoRepository extends MongoRepository<HouseholdMongo, Integer> {

}
