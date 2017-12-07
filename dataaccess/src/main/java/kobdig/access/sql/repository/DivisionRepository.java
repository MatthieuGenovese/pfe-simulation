package kobdig.access.sql.repository;

import kobdig.access.sql.tables.DivisionE;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DivisionRepository extends CrudRepository<DivisionE, Integer> {
}
