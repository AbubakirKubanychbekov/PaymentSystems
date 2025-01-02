package optima.kg.paymentsystems.dto.card;

import lombok.*;

import java.math.BigDecimal;

/**
 * @author Abubakir Dev
 */
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter @Setter
public class CardResponseDto {
    private Long id;
    private String cardNumber;
    private BigDecimal balance;
    private Long clientId;
    private String paymentSystem;
}
