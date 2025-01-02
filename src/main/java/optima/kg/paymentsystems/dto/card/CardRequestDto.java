package optima.kg.paymentsystems.dto.card;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

/**
 * @author Abubakir Dev
 */
@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
public class CardRequestDto {

    private String paymentSystem;
    @NotNull
    @Positive
    private BigDecimal amount;
}
