package optima.kg.paymentsystems.services.consumerProducer;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

/**
 * @author Abubakir Dev
 */
@Service
public class CardEventConsumer {
//    @RabbitListener(queues = "card-events")
//    public void listen(String message) {
//        System.out.println("Received message: " + message);
//    }
}
