package kobdig.mongo.repository;

import kobdig.mongo.collections.HouseholdMongo;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

/**
 * Created by Matthieu on 07/12/2017.
 */
public interface HouseholdMongoRepository extends MongoRepository<HouseholdMongo, Integer> {

    public List<HouseholdMongo> findByidSimulation(int idSimulation);

}
