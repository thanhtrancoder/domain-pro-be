package thanhtrancoder.domain_pro_be.common.exceptions;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import thanhtrancoder.domain_pro_be.common.entity.ResponseCustom;

import java.time.LocalDateTime;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ResponseCustom> handleException(Exception exception) {
        ResponseCustom responseCustom = new ResponseCustom(
                LocalDateTime.now(),
                500,
                exception.getMessage(),
                null
        );

        return ResponseEntity.ok(responseCustom);
    }
}
