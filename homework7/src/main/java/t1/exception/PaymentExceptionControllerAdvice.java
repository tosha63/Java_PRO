package t1.exception;

import org.apache.coyote.BadRequestException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import t1.dto.PaymentErrorResponseDto;

@RestControllerAdvice
public class PaymentExceptionControllerAdvice {

    @ExceptionHandler(IntegrationException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public PaymentErrorResponseDto handleEntityNotFoundException(IntegrationException e) {
        return new PaymentErrorResponseDto(e.getReason(), e.getMessage());
    }

    @ExceptionHandler(BadRequestException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public PaymentErrorResponseDto handleBadRequestException(BadRequestException e) {
        return new PaymentErrorResponseDto("Передана сумма больше чем на балансе", e.getMessage());
    }
}
