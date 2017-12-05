package kobdig.access.repository;

import kobdig.access.tables.TransportNetworkE;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransportNetworkRepository extends CrudRepository<TransportNetworkE, Integer> {

    @Query(value = "SELECT * FROM Red_primaria r WHERE r.gid =176 OR r.gid = 784 OR r.gid = 794 OR r.gid = 793 OR r.gid = 798 OR r.gid = 796 OR r.gid = 822 OR r.gid = 819 OR r.gid = 856 OR r.gid = 852 OR r.gid = 849 OR r.gid = 885 OR r.gid = 894 OR r.gid = 891 OR r.gid = 937 OR r.gid = 932 OR r.gid = 938 OR r.gid = 984 OR r.gid = 990 OR r.gid = 986 OR r.gid = 1029 OR r.gid = 1028 OR r.gid = 1076 OR r.gid = 1077 OR r.gid = 1113 OR r.gid = 1114 OR r.gid =1117 OR r.gid = 1165 OR r.gid = 1164 OR r.gid = 1218 OR r.gid = 1221 OR r.gid =1220 OR r.gid =1280 OR r.gid = 1281 OR r.gid = 1284 OR r.gid = 1332 OR r.gid = 1330 OR r.gid = 1373 OR r.gid = 1368 OR r.gid = 1374 OR r.gid = 1418 OR r.gid = 1416 OR r.gid = 1455 OR r.gid = 1453 OR r.gid = 1487 OR r.gid = 1533 OR r.gid = 1527 OR r.gid = 51 OR r.gid = 48 OR r.gid = 52 OR r.gid = 64 OR r.gid = 63 OR r.gid = 76 OR r.gid = 94 OR r.gid = 91 OR r.gid = 90 OR r.gid = 96 OR r.gid = 102 OR r.gid = 101 OR r.gid = 106 OR r.gid = 110 OR r.gid = 109 OR r.gid = 114 OR r.gid = 113 OR r.gid = 118 OR r.gid = 117 OR r.gid = 122", nativeQuery = true)
    List<TransportNetworkE> findById();


    @Query(value = "SELECT * FROM (SELECT * FROM Red_primaria r WHERE r.gid =176 OR r.gid = 784 OR r.gid = 794 OR r.gid = 793 OR r.gid = 798 OR r.gid = 796 OR r.gid = 822 OR r.gid = 819 OR r.gid = 856 OR r.gid = 852 OR r.gid = 849 OR r.gid = 885 OR r.gid = 894 OR r.gid = 891 OR r.gid = 937 OR r.gid = 932 OR r.gid = 938 OR r.gid = 984 OR r.gid = 990 OR r.gid = 986 OR r.gid = 1029 OR r.gid = 1028 OR r.gid = 1076 OR r.gid = 1077 OR r.gid = 1113 OR r.gid = 1114 OR r.gid =1117 OR r.gid = 1165 OR r.gid = 1164 OR r.gid = 1218 OR r.gid = 1221 OR r.gid =1220 OR r.gid =1280 OR r.gid = 1281 OR r.gid = 1284 OR r.gid = 1332 OR r.gid = 1330 OR r.gid = 1373 OR r.gid = 1368 OR r.gid = 1374 OR r.gid = 1418 OR r.gid = 1416 OR r.gid = 1455 OR r.gid = 1453 OR r.gid = 1487 OR r.gid = 1533 OR r.gid = 1527 OR r.gid = 51 OR r.gid = 48 OR r.gid = 52 OR r.gid = 64 OR r.gid = 63 OR r.gid = 76 OR r.gid = 94 OR r.gid = 91 OR r.gid = 90 OR r.gid = 96 OR r.gid = 102 OR r.gid = 101 OR r.gid = 106 OR r.gid = 110 OR r.gid = 109 OR r.gid = 114 OR r.gid = 113 OR r.gid = 118 OR r.gid = 117 OR r.gid = 122) a INNER JOIN buffer b ON ST_Intersects(a.geom, b.geom) WHERE b.id_land = :id", nativeQuery = true)
    List<TransportNetworkE> findById(@Param("id") int id);
}
