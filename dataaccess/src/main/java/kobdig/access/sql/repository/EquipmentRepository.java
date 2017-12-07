package kobdig.access.sql.repository;

import kobdig.access.sql.tables.EquipmentE;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface EquipmentRepository extends CrudRepository<EquipmentE, Integer> {

    /**
     *
     * = 85 OR e.codigo_upz = 81 OR e.codigo_upz = 80 OR e.codigo_upz = 46 OR e.codigo_upz = 112 OR e.codigo_upz = 116 OR e.codigo_upz = 31 OR e.codigo_upz = 30 OR e.codigo_upz = 29 OR e.codigo_upz = 28 OR e.codigo_upz = 27
     */
    @Query(value = "SELECT * FROM Equipamentos e WHERE e.codigo_upz in :ids", nativeQuery = true)
    List<EquipmentE> findByCodigo_Upz(@Param("ids")List<Integer> list);

    @Query(value = "SELECT * FROM (SELECT * FROM Equipamentos e WHERE e.codigo_upz in :ids) a INNER JOIN buffer b ON ST_Intersects(a.geom, b.geom) WHERE b.id_land = :id", nativeQuery = true)
    List<EquipmentE> findById(@Param("ids")List<Integer> list,@Param("id")int id);
}
