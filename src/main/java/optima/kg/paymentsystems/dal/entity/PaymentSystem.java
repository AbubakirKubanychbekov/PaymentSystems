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
public class PaymentSystem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @OneToMany(mappedBy = "paymentSystem", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Card> cards = new ArrayList<>();

    public List<Card> getCards() {
        return cards;
    }

    public void setCards(List<Card> cards) {
        this.cards = cards;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "PaymentSystem{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
