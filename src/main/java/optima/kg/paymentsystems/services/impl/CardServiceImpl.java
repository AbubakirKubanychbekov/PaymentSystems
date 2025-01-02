package optima.kg.paymentsystems.services.impl;

import lombok.RequiredArgsConstructor;
import optima.kg.paymentsystems.dal.entity.Card;
import optima.kg.paymentsystems.dal.entity.Client;
import optima.kg.paymentsystems.dal.entity.PaymentSystem;
import optima.kg.paymentsystems.dal.repository.CardRepository;
import optima.kg.paymentsystems.dal.repository.ClientRepository;
import optima.kg.paymentsystems.dal.repository.PaymentSystemRepository;
import optima.kg.paymentsystems.dto.card.CardRequestDto;
import optima.kg.paymentsystems.dto.card.CardResponseDto;
import optima.kg.paymentsystems.exceptions.BadCredentialException;
import optima.kg.paymentsystems.exceptions.NotFoundException;
import optima.kg.paymentsystems.services.CardService;
import optima.kg.paymentsystems.services.consumerProducer.CardEventProducer;
import optima.kg.paymentsystems.strategy.ProcessingCenter;
import optima.kg.paymentsystems.strategy.impl.ProcessingCenterFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

/**
 * @author Abubakir Dev
 */
@Service
@RequiredArgsConstructor
public class CardServiceImpl implements CardService {
    private final ClientRepository clientRepository;
    private final CardRepository cardRepository;
    private final ProcessingCenterFactory processingCenterFactory;
    private final PaymentSystemRepository paymentSystemRepository;
    private final CardEventProducer cardEventProducer;
    private final Logger log = LoggerFactory.getLogger(CardServiceImpl.class);


    /**
     * Issues a new card for the specified client with the provided payment system and amount and publishes an event to RabbitMQ.
     *
     * @param clientId ID of the client.
     * @param card     Request DTO containing payment system and amount information.
     * @return Response DTO with details of the issued card.
     * @throws NotFoundException      if the client or payment system is not found.
     * @throws BadCredentialException if the payment system is unsupported.
     */
    @Transactional
    @Override
    public CardResponseDto issueCard(Long clientId, CardRequestDto card) {
        Client client = clientRepository.findById(clientId)
                .orElseThrow(() -> new NotFoundException("Client with id: %s not found".formatted(clientId)));

        PaymentSystem paymentSystem = paymentSystemRepository.findByNameIgnoreCase(card.getPaymentSystem())
                .orElseThrow(() -> new NotFoundException("Payment system %s not found".formatted(card.getPaymentSystem())));

        Card newCard = new Card();
        newCard.setBalance(card.getAmount());
        newCard.setClient(client);
        newCard.setPaymentSystem(paymentSystem);

        ProcessingCenter processingCenter =
                processingCenterFactory.getProcessingCenter(card.getPaymentSystem());

        paymentSystem.getCards().add(newCard);

        processingCenter.issueCard(newCard);
//        cardEventProducer.sendCardEvent("Card created with ID: " + newCard.getId());
        cardRepository.save(newCard);
        log.info("Card " + newCard + " is saved");
        client.getCards().add(newCard);
        clientRepository.save(client);

        return mapToResponseDto(newCard);
    }

    /**
     * Replenishes the balance of the specified card.
     *
     * @param cardId ID of the card to replenish.
     * @param amount Amount to add to the card's balance.
     * @return Response DTO with updated card details.
     * @throws NotFoundException if the card is not found.
     */
    @Override
    public CardResponseDto replenishment(Long cardId, BigDecimal amount) {
        Card card = cardRepository.findById(cardId).orElseThrow(() ->
                new NotFoundException("Card with id: %s not found".formatted(cardId)));
        card.setBalance(card.getBalance().add(amount));
        Card updateCard = cardRepository.save(card);
        log.info("Replenishment to " + card + " successfully");
        return mapToResponseDto(updateCard);
    }

    /**
     * Withdraws funds from the specified card.
     *
     * @param cardId ID of the card to withdraw from.
     * @param amount Amount to withdraw.
     * @return Response DTO with updated card details.
     * @throws NotFoundException      if the card is not found.
     * @throws BadCredentialException if the card has insufficient balance.
     */
    @Override
    public CardResponseDto withdraw(Long cardId, BigDecimal amount) {
        Card card = cardRepository.findById(cardId).orElseThrow(() ->
                new NotFoundException("Card with id: %s not found".formatted(cardId)));
        if (card.getBalance().compareTo(amount) < 0) {
            log.error("Insufficient balance on the card " + card);
            throw new BadCredentialException("Insufficient balance on the card");
        }
        card.setBalance(card.getBalance().subtract(amount));
        Card updateCard = cardRepository.save(card);
        log.info("Withdraw is " + card + " successfully");
        return mapToResponseDto(updateCard);
    }

    /**
     * Maps a Card entity to a CardResponseDto.
     *
     * @param card The Card entity to map.
     * @return The mapped CardResponseDto.
     */
    private CardResponseDto mapToResponseDto(Card card) {
        return CardResponseDto.builder()
                .id(card.getId())
                .cardNumber(String.valueOf(card.getCardNumber()))
                .balance(card.getBalance())
                .clientId(card.getClient().getId())
                .paymentSystem(card.getPaymentSystem() != null ? card.getPaymentSystem().getName() : null)
                .build();
    }
}
