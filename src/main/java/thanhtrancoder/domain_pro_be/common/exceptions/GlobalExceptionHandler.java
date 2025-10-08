package thanhtrancoder.domain_pro_be.common.exceptions;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import thanhtrancoder.domain_pro_be.common.entity.ResponseCustom;
import thanhtrancoder.domain_pro_be.common.entity.ResponseCustomService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@ControllerAdvice
public class GlobalExceptionHandler {
    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @Autowired
    private ResponseCustomService res;

    @ExceptionHandler(AuthorizationDeniedException.class)
    public ResponseEntity<ResponseCustom<Object>> handleAuthorizationDeniedException(
            AuthorizationDeniedException exception
    ) {
        logger.error("Access denied");

        return res.accessDenied("Bạn không có quyền truy cập tài nguyên này.");
    }

    @ExceptionHandler(CustomException.class)
    public ResponseEntity<ResponseCustom<Object>> handleCustomException(CustomException exception) {
        return res.fail(exception.getMessage());
    }

    @ExceptionHandler(QueryException.class)
    public ResponseEntity<ResponseCustom<Object>> handleQueryException(QueryException exception) {
        logger.error("Server error", exception);
        return res.unavailable(exception.getMessage());
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ResponseCustom<Object>> handleHttpMessageNotReadableException(
            HttpMessageNotReadableException exception
    ) {
        logger.error("Request error", exception);
        return res.badRequest("Nội dung yêu cầu không hợp lệ.");
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ResponseCustom<Object>> handleException(Exception exception) {
        logger.error("Server error", exception);
        return res.error("Có lỗi xảy ra trong quá trình xử lý.");
    }
}
