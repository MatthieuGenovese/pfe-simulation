package kobdig.repository;

import kobdig.tables.HouseholdE;
import kobdig.tables.InvestorE;
import kobdig.tables.PromoterE;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PromoterRepository extends CrudRepository<PromoterE, Integer> {

    @Query(value = "SELECT * FROM Promoters p limit :nbr", nativeQuery = true)
    List<PromoterE> findByNbr(@Param("nbr")int nbr);
}
