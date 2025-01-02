package optima.kg.paymentsystems.dto.paymentSystem;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

/**
 * @author Abubakir Dev
 */
@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
public class PaymentSystemResponseDto {
    private Long id;
    private String name;
    private List<Long>cardIds;
}
