package kobdig.mongo.repository;

import kobdig.mongo.collections.PropertyMongo;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * Created by Matthieu on 07/12/2017.
 */
public interface PropertyMongoRepository extends MongoRepository<PropertyMongo, Integer> {

}
