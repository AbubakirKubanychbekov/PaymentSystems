package optima.kg.paymentsystems.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.http.HttpStatus;

/**
 * @author Abubakir Dev
 */

@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
public class SimpleResponse {
    private HttpStatus httpStatus;
    private String message;
}
