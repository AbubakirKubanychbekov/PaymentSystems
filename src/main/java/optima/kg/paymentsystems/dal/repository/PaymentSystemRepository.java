package optima.kg.paymentsystems.dal.repository;

import optima.kg.paymentsystems.dal.entity.PaymentSystem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * @author Abubakir Dev
 */
@Repository
public interface PaymentSystemRepository extends JpaRepository<PaymentSystem,Long> {

    @Query("SELECT p FROM PaymentSystem p WHERE LOWER(p.name) = LOWER(:name)")
    Optional<PaymentSystem> findByNameIgnoreCase(@Param("name") String name);

}
