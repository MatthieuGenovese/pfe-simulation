package kobdig.sql.repository;

import kobdig.sql.tables.Sauvegarde;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SauvegardeRepository extends CrudRepository<Sauvegarde, Integer> {
}
