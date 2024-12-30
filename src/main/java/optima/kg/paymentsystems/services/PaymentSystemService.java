package optima.kg.paymentsystems.services;

import optima.kg.paymentsystems.dto.SimpleResponse;
import optima.kg.paymentsystems.dto.paymentSystem.PaymentSystemRequestDto;
import optima.kg.paymentsystems.dto.paymentSystem.PaymentSystemResponseDto;

import java.util.List;

/**
 * @author Abubakir Dev
 */
public interface PaymentSystemService {
    PaymentSystemResponseDto createPaymentSystem(PaymentSystemRequestDto paymentSystem);

    List<PaymentSystemResponseDto> getAllPaymentSystems();

    PaymentSystemResponseDto getPaymentSystemById(Long id);

    PaymentSystemResponseDto updatePaymentSystem(Long id, PaymentSystemRequestDto paymentSystem);

    SimpleResponse deletePaymentSystem(Long id);
}
