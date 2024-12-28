package optima.kg.paymentsystems.dto.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

/**
 * @author Abubakir Dev
 */

@Entity
@Table(name = "clients")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter @Setter
public class Client {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @OneToMany
    private List<Card>cards;
}
