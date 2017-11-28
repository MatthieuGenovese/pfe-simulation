package kobdig.sauvegarde;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Component;

@Component
public interface SauvegardeRepository extends CrudRepository<Sauvegarde, Integer> {
}
