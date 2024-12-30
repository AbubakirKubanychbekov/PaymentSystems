package optima.kg.paymentsystems.strategy.impl;

import optima.kg.paymentsystems.exceptions.BadCredentialException;
import optima.kg.paymentsystems.strategy.ProcessingCenter;
import org.springframework.stereotype.Service;

/**
 * @author Abubakir Dev
 */
@Service
public class ProcessingCenterFactory {
    private final VisaProcessingCenter visaProcessingCenter;
    private final MasterCardProcessingCenter masterCardProcessingCenter;
    private final ElcartProcessingCenter elcartProcessingCenter;

    public ProcessingCenterFactory(
            VisaProcessingCenter visaProcessingCenter,
            MasterCardProcessingCenter masterCardProcessingCenter,
            ElcartProcessingCenter elcartProcessingCenter) {
        this.visaProcessingCenter = visaProcessingCenter;
        this.masterCardProcessingCenter = masterCardProcessingCenter;
        this.elcartProcessingCenter = elcartProcessingCenter;
    }

    /**
     * Returns the appropriate {@link ProcessingCenter} based on the payment system name.
     *
     * @param paymentSystemName the name of the payment system (e.g., "Visa", "MasterCard", "Elcart").
     * @return the corresponding {@link ProcessingCenter} implementation.
     * @throws BadCredentialException if the provided payment system name is unsupported.
     */
    public ProcessingCenter getProcessingCenter(String paymentSystemName) {
        return switch (paymentSystemName.toLowerCase()) {
            case "visa" -> visaProcessingCenter;
            case "mastercard" -> masterCardProcessingCenter;
            case "elcart" -> elcartProcessingCenter;
            default -> throw new BadCredentialException("Unsupported payment system: " + paymentSystemName);
        };
    }
}
