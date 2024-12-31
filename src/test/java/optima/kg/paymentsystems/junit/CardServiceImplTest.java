package optima.kg.paymentsystems.junit;

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
import optima.kg.paymentsystems.services.consumerProducer.CardEventProducer;
import optima.kg.paymentsystems.services.impl.CardServiceImpl;
import optima.kg.paymentsystems.strategy.ProcessingCenter;
import optima.kg.paymentsystems.strategy.impl.ProcessingCenterFactory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

/**
 * @author Abubakir Dev
 */
@ExtendWith(MockitoExtension.class)
public class CardServiceImplTest {

    @Mock
    private ClientRepository clientRepository;

    @Mock
    private CardRepository cardRepository;

    @Mock
    private ProcessingCenterFactory processingCenterFactory;

    @Mock
    private PaymentSystemRepository paymentSystemRepository;

    @Mock
    private CardEventProducer cardEventProducer;

    @InjectMocks
    private CardServiceImpl cardService;

    @Test
    void issueCard() {
        Long clientId = 1L;
        CardRequestDto cardRequestDto = new CardRequestDto("Visa", BigDecimal.valueOf(1000));
        Client client = new Client();
        client.setId(clientId);
        client.setName("Abubakir Kubanychbekov");

        PaymentSystem paymentSystem = new PaymentSystem();
        paymentSystem.setName("Visa");

        Card mockCard = new Card();
        mockCard.setBalance(BigDecimal.valueOf(1000));
        mockCard.setClient(client);
        mockCard.setPaymentSystem(paymentSystem);

        ProcessingCenter processingCenter = Mockito.mock(ProcessingCenter.class);

        Mockito.when(clientRepository.findById(clientId)).thenReturn(Optional.of(client));
        Mockito.when(paymentSystemRepository.findByNameIgnoreCase("Visa")).thenReturn(Optional.of(paymentSystem));
        Mockito.when(processingCenterFactory.getProcessingCenter("Visa")).thenReturn(processingCenter);

        Mockito.when(cardRepository.save(Mockito.any(Card.class))).thenAnswer(invocation -> {
            Card card = invocation.getArgument(0);
            card.setId(1L);
            return card;
        });

        CardResponseDto result = cardService.issueCard(clientId, cardRequestDto);

        Assertions.assertNotNull(result);
        Assertions.assertEquals(1L, result.getId());
        Assertions.assertEquals(BigDecimal.valueOf(1000), result.getBalance());
        Assertions.assertEquals("Visa", result.getPaymentSystem());
//        Mockito.verify(cardEventProducer, Mockito.times(1))
//                .sendCardEvent("Card created with ID: " + mockCard.getId());
        Mockito.verify(cardRepository, Mockito.times(1)).save(Mockito.any(Card.class));
    }

    @Test
    void issueCardNotFoundClientException() {
        Long clientId = 1L;

        CardRequestDto cardRequestDto = new CardRequestDto("Visa", BigDecimal.valueOf(1000));
        Mockito.when(clientRepository.findById(clientId)).thenReturn(Optional.empty());
        NotFoundException exception = Assertions.assertThrows(NotFoundException.class, () ->
                cardService.issueCard(clientId, cardRequestDto));
        Assertions.assertEquals("Client with id: 1 not found", exception.getMessage());
//        Mockito.verify(cardEventProducer, Mockito.never()).sendCardEvent(Mockito.anyString());
    }

    @Test
    void issueCardNotFoundPaymentSystemException() {
        Long clientId = 1L;

        CardRequestDto cardRequestDto = new CardRequestDto("Visa", BigDecimal.valueOf(1000));
        Client mockClient = new Client();
        mockClient.setId(clientId);
        Mockito.when(clientRepository.findById(clientId)).thenReturn(Optional.of(mockClient));
        Mockito.when(paymentSystemRepository.findByNameIgnoreCase("Visa")).thenReturn(Optional.empty());

        NotFoundException exception = Assertions.assertThrows(NotFoundException.class, () ->
                cardService.issueCard(clientId, cardRequestDto));
        Assertions.assertEquals("Payment system Visa not found", exception.getMessage());
//        Mockito.verify(cardEventProducer, Mockito.never()).sendCardEvent(Mockito.anyString());
    }

    @Test
    void issueCardBadCredentialException() {
        Long clientId = 1L;

        CardRequestDto cardRequestDto = new CardRequestDto("UnsupportedSystem", BigDecimal.valueOf(1000));
        Client client = new Client();
        client.setId(clientId);
        PaymentSystem paymentSystem = new PaymentSystem();
        paymentSystem.setName("UnsupportedSystem");
        Mockito.when(clientRepository.findById(clientId)).thenReturn(Optional.of(client));
        Mockito.when(paymentSystemRepository.findByNameIgnoreCase("UnsupportedSystem"))
                .thenReturn(Optional.of(paymentSystem));
        Mockito.when(processingCenterFactory.getProcessingCenter("UnsupportedSystem"))
                .thenThrow(new BadCredentialException("Unsupported payment system"));
        BadCredentialException exception = Assertions.assertThrows(BadCredentialException.class, () ->
                cardService.issueCard(clientId, cardRequestDto));
        Assertions.assertEquals("Unsupported payment system", exception.getMessage());
    }


    @Test
    void replenishment() {
        Long cardId = 1L;
        BigDecimal amountToReplenish = BigDecimal.valueOf(500);

        // Создаем клиента и карту
        Client client = new Client();
        client.setId(1L);
        client.setName("Abubakir Kubanychbekov");

        Card card = new Card();
        card.setId(cardId);
        card.setBalance(BigDecimal.valueOf(1000));
        card.setClient(client);

        Mockito.when(cardRepository.findById(cardId)).thenReturn(Optional.of(card));
        Mockito.when(cardRepository.save(Mockito.any(Card.class))).thenAnswer(invocation -> invocation.getArgument(0));

        CardResponseDto result = cardService.replenishment(cardId, amountToReplenish);

        Assertions.assertNotNull(result);
        Assertions.assertEquals(BigDecimal.valueOf(1500), result.getBalance());
        Assertions.assertEquals(client.getId(), result.getClientId());
        Mockito.verify(cardRepository, Mockito.times(1)).save(Mockito.any(Card.class));
    }



    @Test
    void withdraw() {
        Long cardId = 1L;
        BigDecimal amountToWithdraw = BigDecimal.valueOf(500);

        // Создаем клиента и карту
        Client client = new Client();
        client.setId(1L);
        client.setName("Abubakir Kubanychbekov");

        Card card = new Card();
        card.setId(cardId);
        card.setBalance(BigDecimal.valueOf(1000));
        card.setClient(client);

        Mockito.when(cardRepository.findById(cardId)).thenReturn(Optional.of(card));
        Mockito.when(cardRepository.save(Mockito.any(Card.class))).thenAnswer(invocation -> invocation.getArgument(0));

        CardResponseDto result = cardService.withdraw(cardId, amountToWithdraw);

        Assertions.assertNotNull(result);
        Assertions.assertEquals(BigDecimal.valueOf(500), result.getBalance());
        Assertions.assertEquals(client.getId(), result.getClientId());
        Mockito.verify(cardRepository, Mockito.times(1)).save(Mockito.any(Card.class));
    }


    @Test
    void withdrawExceptionInsufficientBalance() {
        Long cardId = 1L;
        BigDecimal amountToWithdraw = BigDecimal.valueOf(1500);
        Card mockCard = new Card();
        mockCard.setId(cardId);
        mockCard.setBalance(BigDecimal.valueOf(1000));

        Mockito.when(cardRepository.findById(cardId)).thenReturn(Optional.of(mockCard));

        Assertions.assertThrows(BadCredentialException.class, () -> {
            cardService.withdraw(cardId, amountToWithdraw);
        });
    }
}
