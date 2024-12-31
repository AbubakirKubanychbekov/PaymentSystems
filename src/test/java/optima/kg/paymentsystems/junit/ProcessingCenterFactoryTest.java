package optima.kg.paymentsystems.junit;

import optima.kg.paymentsystems.exceptions.BadCredentialException;
import optima.kg.paymentsystems.strategy.ProcessingCenter;
import optima.kg.paymentsystems.strategy.impl.ElcartProcessingCenter;
import optima.kg.paymentsystems.strategy.impl.MasterCardProcessingCenter;
import optima.kg.paymentsystems.strategy.impl.ProcessingCenterFactory;
import optima.kg.paymentsystems.strategy.impl.VisaProcessingCenter;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

/**
 * @author Abubakir Dev
 */
@ExtendWith(MockitoExtension.class)
public class ProcessingCenterFactoryTest {
    @Mock
    private VisaProcessingCenter visaProcessingCenter;
    @Mock
    private MasterCardProcessingCenter masterCardProcessingCenter;
    @Mock
    private ElcartProcessingCenter elcartProcessingCenter;
    @InjectMocks
    private ProcessingCenterFactory processingCenterFactory;

    @Test
    void getProcessingCenterVisa() {
        ProcessingCenter processingCenter = processingCenterFactory.getProcessingCenter("Visa");
        Assertions.assertEquals(visaProcessingCenter, processingCenter);
    }

    @Test
    void getProcessingCenterMasterCard() {
        ProcessingCenter processingCenter = processingCenterFactory.getProcessingCenter("MasterCard");
        Assertions.assertEquals(masterCardProcessingCenter, processingCenter);
    }

    @Test
    void getProcessingCenterEclart() {
        ProcessingCenter processingCenter = processingCenterFactory.getProcessingCenter("Elcart");
        Assertions.assertEquals(elcartProcessingCenter, processingCenter);
    }

    @Test
    void getProcessingCenterBadCredentialException() {
        BadCredentialException exception = Assertions.assertThrows(
                BadCredentialException.class,
                () -> processingCenterFactory.getProcessingCenter("unsupported")
        );
        Assertions.assertEquals("Unsupported payment system: unsupported", exception.getMessage());
    }
}
