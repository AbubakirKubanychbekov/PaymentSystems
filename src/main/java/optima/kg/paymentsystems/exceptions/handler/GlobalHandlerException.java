package optima.kg.paymentsystems.exceptions.handler;

import optima.kg.paymentsystems.exceptions.BadCredentialException;
import optima.kg.paymentsystems.exceptions.ExceptionResponse;
import optima.kg.paymentsystems.exceptions.NotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalHandlerException {

    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ExceptionResponse notFoundException(NotFoundException e) {
        return new ExceptionResponse(
                HttpStatus.NOT_FOUND,
                e.getClass().getSimpleName(),
                e.getMessage());
    }

    @ExceptionHandler(BadCredentialException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ExceptionResponse badCredentialForbidden(BadCredentialException e) {
        return new ExceptionResponse(
                HttpStatus.FORBIDDEN,
                e.getClass().getSimpleName(),
                e.getMessage());
    }
}
