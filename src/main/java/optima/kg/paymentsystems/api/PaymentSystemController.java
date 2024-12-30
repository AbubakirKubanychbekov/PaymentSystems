package optima.kg.paymentsystems.api;

import optima.kg.paymentsystems.dto.SimpleResponse;
import optima.kg.paymentsystems.dto.paymentSystem.PaymentSystemRequestDto;
import optima.kg.paymentsystems.dto.paymentSystem.PaymentSystemResponseDto;
import optima.kg.paymentsystems.services.PaymentSystemService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author Abubakir Dev
 */
@RestController
@RequestMapping("/api/payment-systems")
public class PaymentSystemController {
    private final PaymentSystemService paymentSystemService;

    public PaymentSystemController(PaymentSystemService paymentSystemService) {
        this.paymentSystemService = paymentSystemService;
    }

    @PostMapping
    public PaymentSystemResponseDto createPaymentSystem(@RequestBody PaymentSystemRequestDto paymentSystem) {
        return paymentSystemService.createPaymentSystem(paymentSystem);
    }

    @GetMapping
    public List<PaymentSystemResponseDto> getAllPaymentSystems() {
        return paymentSystemService.getAllPaymentSystems();
    }

    @GetMapping("/{id}")
    public PaymentSystemResponseDto getPaymentSystemById(@PathVariable Long id) {
        return paymentSystemService.getPaymentSystemById(id);
    }

    @PutMapping("/{id}")
    public PaymentSystemResponseDto updatePaymentSystem(@PathVariable Long id,
                                                        @RequestBody PaymentSystemRequestDto paymentSystem) {
        return paymentSystemService.updatePaymentSystem(id, paymentSystem);
    }

    @DeleteMapping("/{id}")
    public SimpleResponse deletePaymentSystem(@PathVariable Long id) {
       return paymentSystemService.deletePaymentSystem(id);
    }
}
