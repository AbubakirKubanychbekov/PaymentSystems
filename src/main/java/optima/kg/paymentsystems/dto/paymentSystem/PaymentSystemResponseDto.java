package optima.kg.paymentsystems.dto.paymentSystem;

import java.util.List;

/**
 * @author Abubakir Dev
 */
public class PaymentSystemResponseDto {
    private Long id;
    private String name;

    private List<Long>cardIds;

    public List<Long> getCardIds() {
        return cardIds;
    }

    public void setCardIds(List<Long> cardIds) {
        this.cardIds = cardIds;
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
}
