package optima.kg.paymentsystems.dto.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

/**
 * @author Abubakir Dev
 */
@Entity
@Table(name = "cards")
@AllArgsConstructor
@NoArgsConstructor
@Getter @Setter
@Builder
public class Card {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String cardNumber;

    @ManyToOne
    private PaymentSystem paymentSystem;

    private BigDecimal balance;
}
