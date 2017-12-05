package kobdig.access.repository;

import kobdig.access.tables.InvestorE;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface InvestorRepository extends CrudRepository<InvestorE, Integer> {

    @Query(value = "SELECT * FROM Investors i limit :nbr", nativeQuery = true)
    List<InvestorE> findByNbr(@Param("nbr")int nbr);
}

