package optima.kg.paymentsystems.strategy;

import optima.kg.paymentsystems.dal.entity.Card;

import java.math.BigDecimal;

/**
 * @author Abubakir Dev
 */
public interface ProcessingCenter {
    void issueCard(Card card);
    void replenishment(Card card, BigDecimal amount);
    void withdraw(Card card, BigDecimal amount);
}
