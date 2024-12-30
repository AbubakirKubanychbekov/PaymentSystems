package optima.kg.paymentsystems.dto.card;

import lombok.Builder;

import java.math.BigDecimal;

/**
 * @author Abubakir Dev
 */
@Builder
public class CardResponseDto {
    private Long id;
    private String cardNumber;
    private BigDecimal balance;
    private Long clientId;
    private String paymentSystem;

    public CardResponseDto(Long id, String cardNumber, BigDecimal balance, Long clientId, String paymentSystem) {
        this.id = id;
        this.cardNumber = cardNumber;
        this.balance = balance;
        this.clientId = clientId;
        this.paymentSystem = paymentSystem;
    }

    public CardResponseDto() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public Long getClientId() {
        return clientId;
    }

    public void setClientId(Long clientId) {
        this.clientId = clientId;
    }

    public String getPaymentSystem() {
        return paymentSystem;
    }

    public void setPaymentSystem(String paymentSystem) {
        this.paymentSystem = paymentSystem;
    }
}
