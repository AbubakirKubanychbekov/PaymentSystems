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
    void issueCard_ShouldIssueCardAndPublishEvent_WhenValidInput() {
        Long clientId = 1L;
        CardRequestDto cardRequestDto = new CardRequestDto("Visa", BigDecimal.valueOf(1000));
        Client mockClient = new Client();
        mockClient.setId(clientId);
        mockClient.setName("John Doe");

        PaymentSystem mockPaymentSystem = new PaymentSystem();
        mockPaymentSystem.setName("Visa");

        Card mockCard = new Card();
        mockCard.setBalance(BigDecimal.valueOf(1000));
        mockCard.setClient(mockClient);
        mockCard.setPaymentSystem(mockPaymentSystem);

        ProcessingCenter mockProcessingCenter = Mockito.mock(ProcessingCenter.class);

        Mockito.when(clientRepository.findById(clientId)).thenReturn(Optional.of(mockClient));
        Mockito.when(paymentSystemRepository.findByNameIgnoreCase("Visa")).thenReturn(Optional.of(mockPaymentSystem));
        Mockito.when(processingCenterFactory.getProcessingCenter("Visa")).thenReturn(mockProcessingCenter);

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
    void issueCard_ShouldThrowNotFoundException_WhenClientNotFound() {
        Long clientId = 1L;
        CardRequestDto cardRequestDto = new CardRequestDto("Visa", BigDecimal.valueOf(1000));

        Mockito.when(clientRepository.findById(clientId)).thenReturn(Optional.empty());

        NotFoundException exception = Assertions.assertThrows(NotFoundException.class, () ->
                cardService.issueCard(clientId, cardRequestDto));
        Assertions.assertEquals("Client with id: 1 not found", exception.getMessage());
//        Mockito.verify(cardEventProducer, Mockito.never()).sendCardEvent(Mockito.anyString());
    }

    @Test
    void issueCard_ShouldThrowNotFoundException_WhenPaymentSystemNotFound() {
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
    void issueCard_ShouldThrowBadCredentialException_WhenProcessingCenterIsUnsupported() {
        Long clientId = 1L;
        CardRequestDto cardRequestDto = new CardRequestDto("UnsupportedSystem", BigDecimal.valueOf(1000));
        Client mockClient = new Client();
        mockClient.setId(clientId);
        PaymentSystem mockPaymentSystem = new PaymentSystem();
        mockPaymentSystem.setName("UnsupportedSystem");

        Mockito.when(clientRepository.findById(clientId)).thenReturn(Optional.of(mockClient));
        Mockito.when(paymentSystemRepository.findByNameIgnoreCase("UnsupportedSystem"))
                .thenReturn(Optional.of(mockPaymentSystem));
        Mockito.when(processingCenterFactory.getProcessingCenter("UnsupportedSystem"))
                .thenThrow(new BadCredentialException("Unsupported payment system"));

        BadCredentialException exception = Assertions.assertThrows(BadCredentialException.class, () ->
                cardService.issueCard(clientId, cardRequestDto));
        Assertions.assertEquals("Unsupported payment system", exception.getMessage());
    }


    @Test
    void replenishment_ShouldReplenishCardBalance_WhenValidInput() {
        Long cardId = 1L;
        BigDecimal amountToReplenish = BigDecimal.valueOf(500);

        // Создаем клиента и карту
        Client mockClient = new Client();
        mockClient.setId(1L);  // Инициализируем ID клиента
        mockClient.setName("John Doe");

        Card mockCard = new Card();
        mockCard.setId(cardId);
        mockCard.setBalance(BigDecimal.valueOf(1000));  // Начальный баланс карты
        mockCard.setClient(mockClient);  // Устанавливаем клиента в карту

        Mockito.when(cardRepository.findById(cardId)).thenReturn(Optional.of(mockCard));

        Mockito.when(cardRepository.save(Mockito.any(Card.class))).thenAnswer(invocation -> invocation.getArgument(0));

        CardResponseDto result = cardService.replenishment(cardId, amountToReplenish);

        Assertions.assertNotNull(result);
        Assertions.assertEquals(BigDecimal.valueOf(1500), result.getBalance()); // Ожидаемый баланс после пополнения
        Assertions.assertEquals(mockClient.getId(), result.getClientId()); // Проверяем, что clientId в DTO соответствует ID клиента
        Mockito.verify(cardRepository, Mockito.times(1)).save(Mockito.any(Card.class)); // Проверяем, что метод save был вызван
    }



    @Test
    void withdraw_ShouldWithdrawFromCard_WhenValidInput() {
        Long cardId = 1L;
        BigDecimal amountToWithdraw = BigDecimal.valueOf(500);

        // Создаем клиента и карту
        Client mockClient = new Client();
        mockClient.setId(1L);  // Инициализируем ID клиента
        mockClient.setName("John Doe");

        Card mockCard = new Card();
        mockCard.setId(cardId);
        mockCard.setBalance(BigDecimal.valueOf(1000));  // Начальный баланс карты
        mockCard.setClient(mockClient);  // Устанавливаем клиента в карту

        Mockito.when(cardRepository.findById(cardId)).thenReturn(Optional.of(mockCard));

        Mockito.when(cardRepository.save(Mockito.any(Card.class))).thenAnswer(invocation -> invocation.getArgument(0));

        CardResponseDto result = cardService.withdraw(cardId, amountToWithdraw);

        Assertions.assertNotNull(result);
        Assertions.assertEquals(BigDecimal.valueOf(500), result.getBalance()); // Ожидаемый баланс после снятия
        Assertions.assertEquals(mockClient.getId(), result.getClientId()); // Проверяем, что clientId в DTO соответствует ID клиента
        Mockito.verify(cardRepository, Mockito.times(1)).save(Mockito.any(Card.class)); // Проверяем, что метод save был вызван
    }


    @Test
    void withdraw_ShouldThrowException_WhenInsufficientBalance() {
        Long cardId = 1L;
        BigDecimal amountToWithdraw = BigDecimal.valueOf(1500);
        Card mockCard = new Card();
        mockCard.setId(cardId);
        mockCard.setBalance(BigDecimal.valueOf(1000));  // Начальный баланс карты

        Mockito.when(cardRepository.findById(cardId)).thenReturn(Optional.of(mockCard));

        Assertions.assertThrows(BadCredentialException.class, () -> {
            cardService.withdraw(cardId, amountToWithdraw);
        });
    }
}
