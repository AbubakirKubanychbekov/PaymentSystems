package optima.kg.paymentsystems.dto;

import org.springframework.http.HttpStatus;

/**
 * @author Abubakir Dev
 */

public class SimpleResponse {
    private HttpStatus httpStatus;
    private String message;

    public SimpleResponse(HttpStatus httpStatus, String message) {
        this.httpStatus = httpStatus;
        this.message = message;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

    public void setHttpStatus(HttpStatus httpStatus) {
        this.httpStatus = httpStatus;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}