package kobdig.mongo.repository;

import kobdig.mongo.collections.PromoterMongo;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * Created by Matthieu on 08/12/2017.
 */
public interface PromoterMongoRepository  extends MongoRepository<PromoterMongo, Integer> {

}
