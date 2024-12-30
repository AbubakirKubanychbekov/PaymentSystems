package optima.kg.paymentsystems.services;

import optima.kg.paymentsystems.dal.entity.Card;
import optima.kg.paymentsystems.dto.card.CardRequestDto;
import optima.kg.paymentsystems.dto.card.CardResponseDto;

import java.math.BigDecimal;

/**
 * @author Abubakir Dev
 */
public interface CardService {
    CardResponseDto issueCard(Long clientId, CardRequestDto card);

    CardResponseDto replenishment(Long cardId, BigDecimal amount);

    CardResponseDto withdraw(Long cardId, BigDecimal amount);
}
