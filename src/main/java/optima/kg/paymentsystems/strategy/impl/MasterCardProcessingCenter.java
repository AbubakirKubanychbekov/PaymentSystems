package optima.kg.paymentsystems.strategy.impl;

import optima.kg.paymentsystems.dal.entity.Card;
import optima.kg.paymentsystems.exceptions.BadCredentialException;
import optima.kg.paymentsystems.strategy.ProcessingCenter;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

/**
 * @author Abubakir Dev
 */
@Service
public class MasterCardProcessingCenter implements ProcessingCenter {
    @Override
    public void issueCard(Card card) {
        card.setCardNumber("MC-" + System.currentTimeMillis());
    }

    @Override
    public void replenishment(Card card, BigDecimal amount) {
        BigDecimal newBalance = card.getBalance().add(amount);
        card.setBalance(newBalance);
    }

    @Override
    public void withdraw(Card card, BigDecimal amount) {
        if (card.getBalance().compareTo(amount) < 0){
            throw new BadCredentialException("Insufficient funds");
        }
        BigDecimal newBalance = card.getBalance().subtract(amount);
        card.setBalance(newBalance);
    }
}
