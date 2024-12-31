package optima.kg.paymentsystems.dal.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Abubakir Dev
 */

@Entity
@Table(name = "clients")
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Client {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name; // Поле для хранения имени клиента. Выбрано для простоты, так как нет строгих требований к детализации клиента.
    //Пока что предполагается, что имя достаточно для идентификации клиента. При необходимости можно добавить дополнительные атрибуты (например, фамилия или телномер email).

    @OneToMany(mappedBy = "client", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Card> cards = new ArrayList<>();

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

    public List<Card> getCards() {
        return cards;
    }

    public void setCards(List<Card> cards) {
        this.cards = cards;
    }

    @Override
    public String toString() {
        return "Client " +
                "id=" + id +
                ", name='" + name;
    }
}
