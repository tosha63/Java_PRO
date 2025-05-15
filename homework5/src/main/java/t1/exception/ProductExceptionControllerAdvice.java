package t1.exception;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import t1.dto.ProductErrorResponseDto;

@RestControllerAdvice
public class ProductExceptionControllerAdvice {

    @ExceptionHandler(EntityNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ProductErrorResponseDto handleEntityNotFoundException(EntityNotFoundException e) {
        return new ProductErrorResponseDto("Произошла ошибка при поиске сущности", e.getMessage());
    }
}
