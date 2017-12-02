package kobdig.repository;

import kobdig.tables.HouseholdE;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HouseholdRepository extends CrudRepository<HouseholdE, Integer> {

}
