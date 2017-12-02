package kobdig.repository;

import kobdig.tables.InvestorE;
import kobdig.tables.PromoterE;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

@Repository
public interface PromoterRepository extends CrudRepository<PromoterE, Integer> {
}
