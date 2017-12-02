package kobdig.repository;

import kobdig.tables.DivisionE;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

@Repository
public interface DivisionRepository extends CrudRepository<DivisionE, Integer> {
}
