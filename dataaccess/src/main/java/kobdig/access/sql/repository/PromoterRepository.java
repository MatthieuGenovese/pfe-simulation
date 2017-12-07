package kobdig.access.sql.repository;

import kobdig.access.sql.tables.PromoterE;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PromoterRepository extends CrudRepository<PromoterE, Integer> {

    @Query(value = "SELECT * FROM Promoters p limit :nbr", nativeQuery = true)
    List<PromoterE> findByNbr(@Param("nbr")int nbr);
}
