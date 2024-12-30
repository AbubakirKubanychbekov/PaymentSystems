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
    void getAllPaymentSystems_ShouldReturnListOfPaymentSystems() {
        PaymentSystem paymentSystem1 = new PaymentSystem();
        paymentSystem1.setId(1L);
        paymentSystem1.setName("System 1");

        PaymentSystem paymentSystem2 = new PaymentSystem();
        paymentSystem2.setId(2L);
        paymentSystem2.setName("System 2");

        Mockito.when(paymentSystemRepository.findAll()).thenReturn(List.of(paymentSystem1, paymentSystem2));

        List<PaymentSystemResponseDto> result = paymentSystemService.getAllPaymentSystems();

        Assertions.assertNotNull(result);
        Assertions.assertEquals(2, result.size());
        Assertions.assertEquals("System 1", result.get(0).getName());
        Assertions.assertEquals("System 2", result.get(1).getName());
    }

    @Test
    void createPaymentSystem_ShouldSavePaymentSystem_WhenValidInput() {
        PaymentSystemRequestDto requestDto = new PaymentSystemRequestDto();
        requestDto.setName("New System");

        PaymentSystem savedPaymentSystem = new PaymentSystem();
        savedPaymentSystem.setId(1L);
        savedPaymentSystem.setName("New System");

        Mockito.when(paymentSystemRepository.save(Mockito.any(PaymentSystem.class))).thenReturn(savedPaymentSystem);

        PaymentSystemResponseDto result = paymentSystemService.createPaymentSystem(requestDto);

        Assertions.assertNotNull(result);
        Assertions.assertEquals(1L, result.getId());
        Assertions.assertEquals("New System", result.getName());
    }

    @Test
    void getPaymentSystemById_ShouldReturnPaymentSystem_WhenExists() {
        Long id = 1L;
        PaymentSystem paymentSystem = new PaymentSystem();
        paymentSystem.setId(id);
        paymentSystem.setName("Existing System");

        Mockito.when(paymentSystemRepository.findById(id)).thenReturn(Optional.of(paymentSystem));

        PaymentSystemResponseDto result = paymentSystemService.getPaymentSystemById(id);

        Assertions.assertNotNull(result);
        Assertions.assertEquals(id, result.getId());
        Assertions.assertEquals("Existing System", result.getName());
    }

    @Test
    void getPaymentSystemById_ShouldThrowNotFoundException_WhenNotFound() {
        Long id = 1L;

        Mockito.when(paymentSystemRepository.findById(id)).thenReturn(Optional.empty());

        NotFoundException exception = Assertions.assertThrows(NotFoundException.class, () -> {
            paymentSystemService.getPaymentSystemById(id);
        });
        Assertions.assertEquals("PaymentSystem with id: 1 not found", exception.getMessage());
    }


    @Test
    void updatePaymentSystem_ShouldUpdatePaymentSystem_WhenExists() {
        Long id = 1L;
        PaymentSystemRequestDto requestDto = new PaymentSystemRequestDto();
        requestDto.setName("Updated System");

        PaymentSystem existingPaymentSystem = new PaymentSystem();
        existingPaymentSystem.setId(id);
        existingPaymentSystem.setName("Old System");

        PaymentSystem updatedPaymentSystem = new PaymentSystem();
        updatedPaymentSystem.setId(id);
        updatedPaymentSystem.setName("Updated System");

        Mockito.when(paymentSystemRepository.findById(id)).thenReturn(Optional.of(existingPaymentSystem));
        Mockito.when(paymentSystemRepository.save(Mockito.any(PaymentSystem.class))).thenReturn(updatedPaymentSystem);

        PaymentSystemResponseDto result = paymentSystemService.updatePaymentSystem(id, requestDto);

        Assertions.assertNotNull(result);
        Assertions.assertEquals("Updated System", result.getName());
    }

    @Test
    void deletePaymentSystem_ShouldDeletePaymentSystem_WhenExists() {
        Long id = 1L;

        Mockito.when(paymentSystemRepository.existsById(id)).thenReturn(true);

        SimpleResponse result = paymentSystemService.deletePaymentSystem(id);

        Assertions.assertNotNull(result);
        Assertions.assertEquals(HttpStatus.OK, result.getHttpStatus());
        Assertions.assertTrue(result.getMessage().contains("success deleted"));
    }

    @Test
    void deletePaymentSystem_ShouldThrowNotFoundException_WhenNotFound() {
        Long id = 1L;

        Mockito.when(paymentSystemRepository.existsById(id)).thenReturn(false);

        NotFoundException exception = Assertions.assertThrows(NotFoundException.class, () -> {
            paymentSystemService.deletePaymentSystem(id);
        });
        Assertions.assertEquals("PaymentSystem with id: 1 not found", exception.getMessage());
    }
}
