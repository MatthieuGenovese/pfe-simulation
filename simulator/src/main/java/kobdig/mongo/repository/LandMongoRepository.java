package kobdig.mongo.repository;

import kobdig.mongo.collections.LandMongo;
import kobdig.urbanSimulation.entities.environement.Land;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

/**
 * Created by Matthieu on 08/12/2017.
 */
public interface LandMongoRepository  extends MongoRepository<LandMongo, Integer> {
    public List<LandMongo> findByidSimulation(int idSimulation);
}