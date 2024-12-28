package optima.kg.paymentsystems.dto.entity;

import jakarta.persistence.*;
import lombok.*;

/**
 * @author Abubakir Dev
 */
@Entity
@Table(name = "payment_system")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter @Setter
public class PaymentSystem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
}
