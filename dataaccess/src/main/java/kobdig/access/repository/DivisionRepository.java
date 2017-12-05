package kobdig.access.repository;

import kobdig.access.tables.DivisionE;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DivisionRepository extends CrudRepository<DivisionE, Integer> {
}
