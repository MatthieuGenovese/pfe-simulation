package kobdig.repository;

import kobdig.tables.InvestorE;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;


@Repository
public interface InvestorRepository extends CrudRepository<InvestorE, Integer> {
}

