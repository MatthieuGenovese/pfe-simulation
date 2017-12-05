package kobdig.access.repository;

import kobdig.access.tables.HouseholdE;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HouseholdRepository extends CrudRepository<HouseholdE, Integer> {

    @Query(value = "SELECT * FROM Households h limit :nbr", nativeQuery = true)
    List<HouseholdE> findByNbr(@Param("nbr")int nbr);
}
