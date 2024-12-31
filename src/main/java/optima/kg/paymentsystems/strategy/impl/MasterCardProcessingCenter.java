package optima.kg.paymentsystems.strategy.impl;

import optima.kg.paymentsystems.dal.entity.Card;
import optima.kg.paymentsystems.exceptions.BadCredentialException;
import optima.kg.paymentsystems.strategy.ProcessingCenter;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Random;

/**
 * @author Abubakir Dev
 */
@Service
public class MasterCardProcessingCenter implements ProcessingCenter {
    @Override
    public void issueCard(Card card) {
        card.setCardNumber("MC-" + System.currentTimeMillis());
        card.setExpirationDate(generateExpirationDate());
        card.setCvv(generateCvv());
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

    private String generateExpirationDate() {
        // Генерация срока годности (например, +3 года от текущей даты)
        LocalDate expiration = LocalDate.now().plusYears(3);
        return expiration.format(DateTimeFormatter.ofPattern("MM/yy"));
    }

    private String generateCvv() {
        // Генерация случайного трехзначного CVV
        return String.format("%03d", new Random().nextInt(1000));
    }
}
