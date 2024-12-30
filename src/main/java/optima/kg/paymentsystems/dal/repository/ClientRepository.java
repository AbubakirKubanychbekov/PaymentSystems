package optima.kg.paymentsystems.dal.repository;

import optima.kg.paymentsystems.dal.entity.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author Abubakir Dev
 */
@Repository
public interface ClientRepository extends JpaRepository<Client,Long> {
}
