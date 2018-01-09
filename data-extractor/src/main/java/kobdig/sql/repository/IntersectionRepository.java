package kobdig.sql.repository;

import kobdig.sql.tables.IntersectionE;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface IntersectionRepository extends CrudRepository<IntersectionE, Integer> {

    @Query(value = "SELECT * FROM Interseccion_upz_redprimaria i where i.id_redprimaria = :id", nativeQuery = true)
    List<IntersectionE> findById_Redprimaria(@Param("id") int id_redprimaria);
}
