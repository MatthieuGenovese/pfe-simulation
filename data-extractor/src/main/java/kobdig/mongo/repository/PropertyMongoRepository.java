package kobdig.mongo.repository;

import kobdig.mongo.collections.PropertyMongo;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

/**
 * Created by Matthieu on 07/12/2017.
 */
public interface PropertyMongoRepository extends MongoRepository<PropertyMongo, Integer> {

    public List<PropertyMongo> findByidSimulation(int idSimulation);

}
