package optima.kg.paymentsystems.dto.client;

import lombok.Data;

import java.util.List;

/**
 * @author Abubakir Dev
 */
public class ClientResponseDto {
    private Long id;
    private String name;
    private List<Long> cardIds;

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

    public List<Long> getCardIds() {
        return cardIds;
    }

    public void setCardIds(List<Long> cardIds) {
        this.cardIds = cardIds;
    }
}
