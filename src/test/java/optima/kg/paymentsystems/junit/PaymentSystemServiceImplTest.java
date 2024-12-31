package optima.kg.paymentsystems.junit;

import optima.kg.paymentsystems.dal.entity.PaymentSystem;
import optima.kg.paymentsystems.dal.repository.PaymentSystemRepository;
import optima.kg.paymentsystems.dto.SimpleResponse;
import optima.kg.paymentsystems.dto.paymentSystem.PaymentSystemRequestDto;
import optima.kg.paymentsystems.dto.paymentSystem.PaymentSystemResponseDto;
import optima.kg.paymentsystems.exceptions.NotFoundException;
import optima.kg.paymentsystems.services.impl.PaymentSystemServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

import java.util.List;
import java.util.Optional;

/**
 * @author Abubakir Dev
 */
@ExtendWith(MockitoExtension.class)
public class PaymentSystemServiceImplTest {
    @Mock
    private PaymentSystemRepository paymentSystemRepository;

    @InjectMocks
    private PaymentSystemServiceImpl paymentSystemService;

    @Test
    void getAllPaymentSystems() {
        PaymentSystem visa = new PaymentSystem();
        visa.setId(1L);
        visa.setName("Visa");

        PaymentSystem elcart = new PaymentSystem();
        elcart.setId(2L);
        elcart.setName("Elcart");

        Mockito.when(paymentSystemRepository.findAll()).thenReturn(List.of(visa, elcart));

        List<PaymentSystemResponseDto> result = paymentSystemService.getAllPaymentSystems();
        Assertions.assertNotNull(result);
        Assertions.assertEquals(2, result.size());
        Assertions.assertEquals("Visa", result.get(0).getName());
        Assertions.assertEquals("Elcart", result.get(1).getName());
    }

    @Test
    void createPaymentSystem() {
        PaymentSystemRequestDto requestDto = new PaymentSystemRequestDto();
        requestDto.setName("New System");

        PaymentSystem savedPaymentSystem = new PaymentSystem();
        savedPaymentSystem.setId(1L);
        savedPaymentSystem.setName("Visa");

        Mockito.when(paymentSystemRepository.save(Mockito.any(PaymentSystem.class)))
                .thenReturn(savedPaymentSystem);

        PaymentSystemResponseDto result = paymentSystemService.createPaymentSystem(requestDto);
        Assertions.assertNotNull(result);
        Assertions.assertEquals(1L, result.getId());
        Assertions.assertEquals("Visa", result.getName());
    }

    @Test
    void getPaymentSystemById() {
        Long id = 1L;

        PaymentSystem paymentSystem = new PaymentSystem();
        paymentSystem.setId(id);
        paymentSystem.setName("Visa");
        Mockito.when(paymentSystemRepository.findById(id)).thenReturn(Optional.of(paymentSystem));
        PaymentSystemResponseDto result = paymentSystemService.getPaymentSystemById(id);
        Assertions.assertNotNull(result);
        Assertions.assertEquals(id, result.getId());
        Assertions.assertEquals("Visa", result.getName());
    }

    @Test
    void getPaymentSystemByIdNotFoundException() {
        Long id = 1L;

        Mockito.when(paymentSystemRepository.findById(id)).thenReturn(Optional.empty());
        NotFoundException exception = Assertions.assertThrows(NotFoundException.class, () -> {
            paymentSystemService.getPaymentSystemById(id);
        });
        Assertions.assertEquals("PaymentSystem with id: 1 not found", exception.getMessage());
    }


    @Test
    void updatePaymentSystem() {
        Long id = 1L;
        PaymentSystemRequestDto requestDto = new PaymentSystemRequestDto();
        requestDto.setName("Updated System");

        PaymentSystem existingPaymentSystem = new PaymentSystem();
        existingPaymentSystem.setId(id);
        existingPaymentSystem.setName("Visa");

        PaymentSystem updatedPaymentSystem = new PaymentSystem();
        updatedPaymentSystem.setId(id);
        updatedPaymentSystem.setName("Elcart");

        Mockito.when(paymentSystemRepository.findById(id))
                .thenReturn(Optional.of(existingPaymentSystem));
        Mockito.when(paymentSystemRepository.save(Mockito.any(PaymentSystem.class)))
                .thenReturn(updatedPaymentSystem);

        PaymentSystemResponseDto result = paymentSystemService.updatePaymentSystem(id, requestDto);
        Assertions.assertNotNull(result);
        Assertions.assertEquals("Elcart", result.getName());
    }

    @Test
    void deletePaymentSystem() {
        Long id = 1L;

        Mockito.when(paymentSystemRepository.existsById(id)).thenReturn(true);
        SimpleResponse result = paymentSystemService.deletePaymentSystem(id);
        Assertions.assertNotNull(result);
        Assertions.assertEquals(HttpStatus.OK, result.getHttpStatus());
        Assertions.assertTrue(result.getMessage().contains("success deleted"));
    }

    @Test
    void deletePaymentSystemNotFoundException() {
        Long id = 1L;

        Mockito.when(paymentSystemRepository.existsById(id)).thenReturn(false);
        NotFoundException exception = Assertions.assertThrows(NotFoundException.class, () -> {
            paymentSystemService.deletePaymentSystem(id);
        });
        Assertions.assertEquals("PaymentSystem with id: 1 not found", exception.getMessage());
    }
}
