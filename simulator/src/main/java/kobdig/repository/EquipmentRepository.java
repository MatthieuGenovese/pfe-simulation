package kobdig.repository;

import kobdig.tables.EquipmentE;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface EquipmentRepository extends CrudRepository<EquipmentE, Integer> {
}
