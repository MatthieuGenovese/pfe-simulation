package kobdig.access.sql.repository;

import kobdig.access.sql.tables.LandE;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LandRepository extends CrudRepository<LandE, Integer> {
}
