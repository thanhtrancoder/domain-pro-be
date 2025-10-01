package thanhtrancoder.domain_pro_be.common.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import thanhtrancoder.domain_pro_be.common.entity.ResponseCustom;

import java.time.LocalDateTime;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(AuthorizationDeniedException.class)
    public ResponseEntity<ResponseCustom> handleException(AuthorizationDeniedException exception) {
        exception.printStackTrace();
        ResponseCustom responseCustom = new ResponseCustom(
                LocalDateTime.now(),
                HttpStatus.FORBIDDEN.value(),
                "Bạn không có quyền truy cập tài nguyên này.",
                null
        );

        return ResponseEntity.status(HttpStatus.FORBIDDEN.value()).body(responseCustom);
    }

    @ExceptionHandler(CustomException.class)
    public ResponseEntity<ResponseCustom> handleException(CustomException exception) {
        ResponseCustom responseCustom = new ResponseCustom(
                LocalDateTime.now(),
                HttpStatus.NOT_FOUND.value(),
                exception.getMessage(),
                null
        );

        return ResponseEntity.status(404).body(responseCustom);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ResponseCustom> handleException(Exception exception) {
        exception.printStackTrace();
        ResponseCustom responseCustom = new ResponseCustom(
                LocalDateTime.now(),
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "Có lỗi xảy ra trong quá trình xử lý",
                null
        );

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR.value()).body(responseCustom);
    }
}
