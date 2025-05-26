package t1.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import t1.dto.LimitResponseDto;

import static t1.dto.State.FAILED;

@RestControllerAdvice
@Slf4j
public class LimitExceptionControllerAdvice {
    @ExceptionHandler(value = Exception.class)
    public LimitResponseDto limitException(Exception e) {
        log.error(e.getMessage(), e);
        return LimitResponseDto.builder()
                               .state(FAILED)
                               .messageInfo(e.getMessage())
                               .build();
    }
}
