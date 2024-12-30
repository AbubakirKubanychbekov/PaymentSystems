package optima.kg.paymentsystems.dal.repository;

import optima.kg.paymentsystems.dal.entity.Card;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author Abubakir Dev
 */
@Repository
public interface CardRepository extends JpaRepository<Card,Long> {
}
