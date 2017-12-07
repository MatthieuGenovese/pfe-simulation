package kobdig.access.sql.repository;

import kobdig.access.sql.tables.Sauvegarde;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SauvegardeRepository extends CrudRepository<Sauvegarde, Integer> {
}
