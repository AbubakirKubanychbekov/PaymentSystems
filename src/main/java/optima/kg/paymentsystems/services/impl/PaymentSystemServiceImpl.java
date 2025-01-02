package optima.kg.paymentsystems.services.impl;

import lombok.RequiredArgsConstructor;
import optima.kg.paymentsystems.dal.entity.Card;
import optima.kg.paymentsystems.dal.entity.PaymentSystem;
import optima.kg.paymentsystems.dal.repository.PaymentSystemRepository;
import optima.kg.paymentsystems.dto.SimpleResponse;
import optima.kg.paymentsystems.dto.paymentSystem.PaymentSystemRequestDto;
import optima.kg.paymentsystems.dto.paymentSystem.PaymentSystemResponseDto;
import optima.kg.paymentsystems.exceptions.NotFoundException;
import optima.kg.paymentsystems.services.PaymentSystemService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Abubakir Dev
 */
@Service
@RequiredArgsConstructor
public class PaymentSystemServiceImpl implements PaymentSystemService {
    private final PaymentSystemRepository paymentSystemRepository;
    private final Logger log = LoggerFactory.getLogger(PaymentSystemServiceImpl.class);


    /**
     * Retrieves all payment systems from the database.
     *
     * @return List of PaymentSystemResponseDto objects containing payment system details.
     */
    @Override
    public List<PaymentSystemResponseDto> getAllPaymentSystems() {
        return paymentSystemRepository.findAll()
                .stream()
                .map(this::mapToResponseDto)
                .collect(Collectors.toList());

    }

    /**
     * Creates a new payment system and saves it to the database.
     *
     * @param paymentSystemDto A PaymentSystemRequestDto containing the payment system's details.
     * @return A PaymentSystemResponseDto containing the created payment system's details.
     */
    @Override
    public PaymentSystemResponseDto createPaymentSystem(PaymentSystemRequestDto paymentSystemDto) {
        PaymentSystem paymentSystem = new PaymentSystem();
        paymentSystem.setName(paymentSystemDto.getName());

        PaymentSystem savedPaymentSystem = paymentSystemRepository.save(paymentSystem);
        log.info("Payment system %s success created".formatted(paymentSystem));
        return mapToResponseDto(savedPaymentSystem);
    }

    /**
     * Retrieves a payment system by its ID.
     *
     * @param id ID of the payment system to retrieve.
     * @return A PaymentSystemResponseDto containing the payment system's details.
     * @throws NotFoundException if the payment system with the given ID is not found.
     */
    @Override
    public PaymentSystemResponseDto getPaymentSystemById(Long id) {
        PaymentSystem paymentSystem = paymentSystemRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("PaymentSystem with id: %s not found".formatted(id)));
        return mapToResponseDto(paymentSystem);
    }

    /**
     * Updates an existing payment system's details.
     *
     * @param id               ID of the payment system to update.
     * @param paymentSystemDto A PaymentSystemRequestDto containing the updated details.
     * @return A PaymentSystemResponseDto containing the updated payment system's details.
     * @throws NotFoundException if the payment system with the given ID is not found.
     */
    @Override
    public PaymentSystemResponseDto updatePaymentSystem(Long id, PaymentSystemRequestDto paymentSystemDto) {
        PaymentSystem paymentSystem = paymentSystemRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("PaymentSystem with id: %s not found".formatted(id)));

        paymentSystem.setName(paymentSystemDto.getName());
        PaymentSystem updatedPaymentSystem = paymentSystemRepository.save(paymentSystem);
        log.info("Payment system %s success is updated".formatted(paymentSystem));
        return mapToResponseDto(updatedPaymentSystem);
    }

    /**
     * Deletes a payment system by its ID.
     *
     * @param id ID of the payment system to delete.
     * @return A SimpleResponse indicating the result of the operation.
     * @throws NotFoundException if the payment system with the given ID is not found.
     */
    @Override
    public SimpleResponse deletePaymentSystem(Long id) {
        if (!paymentSystemRepository.existsById(id)) {
            throw new NotFoundException("PaymentSystem with id: %s not found".formatted(id));
        }
        paymentSystemRepository.deleteById(id);
        log.info("Payment system with id %s success deleted".formatted(id));
        return new SimpleResponse(HttpStatus.OK,
                "PaymentSystem with id: %s success deleted");
    }

    /**
     * Maps a PaymentSystem entity to a PaymentSystemResponseDto.
     *
     * @param paymentSystem The PaymentSystem entity to map.
     * @return A PaymentSystemResponseDto containing the payment system's details.
     */
    private PaymentSystemResponseDto mapToResponseDto(PaymentSystem paymentSystem) {
        PaymentSystemResponseDto responseDto = new PaymentSystemResponseDto();
        responseDto.setId(paymentSystem.getId());
        responseDto.setName(paymentSystem.getName());
        List<Long> cardIds = paymentSystem.getCards()
                .stream()
                .map(Card::getId)
                .collect(Collectors.toList());
        responseDto.setCardIds(cardIds);
        return responseDto;
    }
}
