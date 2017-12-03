package kobdig.repository;

import kobdig.tables.EquipmentE;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface EquipmentRepository extends CrudRepository<EquipmentE, Integer> {

    @Query(value = "SELECT * FROM Equipamentos e WHERE e.codigo_upz IN (85,81,80,46,112,116,31,30,29,28,27)", nativeQuery = true)
    List<EquipmentE> findByCodigo_Upz();
}
