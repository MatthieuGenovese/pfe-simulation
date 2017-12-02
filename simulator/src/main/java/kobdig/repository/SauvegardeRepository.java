package kobdig.repository;

import kobdig.tables.Sauvegarde;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

@Repository
public interface SauvegardeRepository extends CrudRepository<Sauvegarde, Integer> {
}
