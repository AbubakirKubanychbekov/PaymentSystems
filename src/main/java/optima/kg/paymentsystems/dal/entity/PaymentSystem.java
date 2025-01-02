package optima.kg.paymentsystems.dal.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Abubakir Dev
 */
@Entity
@Table(name = "payment_systems")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter @Setter
@ToString
public class PaymentSystem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @OneToMany(mappedBy = "paymentSystem", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Card> cards = new ArrayList<>();
}
