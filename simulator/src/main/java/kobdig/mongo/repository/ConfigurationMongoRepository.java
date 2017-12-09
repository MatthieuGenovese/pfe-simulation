package kobdig.mongo.repository;

import kobdig.mongo.collections.ConfigurationMongo;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * Created by Matthieu on 09/12/2017.
 */
public interface ConfigurationMongoRepository extends MongoRepository<ConfigurationMongo, Integer> {

        }