package kobdig.repository;

import kobdig.tables.TransportNetworkE;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransportNetworkRepository extends CrudRepository<TransportNetworkE, Integer> {

}
