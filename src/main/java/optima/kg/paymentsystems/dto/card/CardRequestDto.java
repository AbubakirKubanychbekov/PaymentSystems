package optima.kg.paymentsystems.dto.card;

import java.math.BigDecimal;

/**
 * @author Abubakir Dev
 */
public class CardRequestDto {
    private String paymentSystem;
    private BigDecimal amount;

    public CardRequestDto() {
    }

    public CardRequestDto(String paymentSystem, BigDecimal amount) {
        this.paymentSystem = paymentSystem;
        this.amount = amount;
    }

    public String getPaymentSystem() {
        return paymentSystem;
    }

    public void setPaymentSystem(String paymentSystem) {
        this.paymentSystem = paymentSystem;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }
}
