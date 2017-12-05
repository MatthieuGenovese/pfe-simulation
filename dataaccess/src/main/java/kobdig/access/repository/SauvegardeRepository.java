package kobdig.access.repository;

import kobdig.access.tables.Sauvegarde;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SauvegardeRepository extends CrudRepository<Sauvegarde, Integer> {
}
