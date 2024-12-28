package optima.kg.paymentsystems.dto.repository;

import optima.kg.paymentsystems.dto.entity.PaymentSystem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author Abubakir Dev
 */
@Repository
public interface PaymentSystemRepository extends JpaRepository<PaymentSystem,Long> {
}
