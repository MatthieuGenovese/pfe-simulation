package kobdig.repository;

import kobdig.tables.EquipmentE;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface EquipmentRepository extends CrudRepository<EquipmentE, Integer> {

    @Query(value = "SELECT * FROM Equipamentos e WHERE e.codigo_upz = 85 OR e.codigo_upz = 81 OR e.codigo_upz = 80 OR e.codigo_upz = 46 OR e.codigo_upz = 112 OR e.codigo_upz = 116 OR e.codigo_upz = 31 OR e.codigo_upz = 30 OR e.codigo_upz = 29 OR e.codigo_upz = 28 OR e.codigo_upz = 27", nativeQuery = true)
    List<EquipmentE> findByCodigo_Upz();
}
