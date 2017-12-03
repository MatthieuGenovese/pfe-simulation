package kobdig.repository;

import kobdig.tables.TransportNetworkE;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransportNetworkRepository extends CrudRepository<TransportNetworkE, Integer> {
/*
    @Query(value = "SELECT r FROM Red_primaria r WHERE r.gid in :ids", nativeQuery = true)
    List<TransportNetworkE> findById(@Param("ids")List<Integer> integerList);
    */

    @Query(value = "SELECT a FROM Red_primaria a INNER JOIN buffer b ON ST_Intersects(a.geom, b.geom) WHERE b.id_land = :id", nativeQuery = true)
    List<TransportNetworkE> findById(@Param("id") int id);
}
