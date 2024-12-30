package optima.kg.paymentsystems.config;

import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author Abubakir Dev
 */
@Configuration
public class RabbitMQConfig {
    @Bean
    public Queue queue() {
        return new Queue("card-events", true);
    }
}
