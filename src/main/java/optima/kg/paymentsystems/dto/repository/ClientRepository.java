package optima.kg.paymentsystems.dto.repository;

import optima.kg.paymentsystems.dto.entity.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author Abubakir Dev
 */
@Repository
public interface ClientRepository extends JpaRepository<Client,Long> {
}
