package kobdig.repository;

import kobdig.tables.IntersectionE;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface IntersectionRepository extends CrudRepository<IntersectionE, Integer> {
}
