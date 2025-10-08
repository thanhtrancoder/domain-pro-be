package thanhtrancoder.domain_pro_be.common.entity;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class ResponseCustomService {
    public <T> ResponseEntity<ResponseCustom<T>> success(String message, T t) {
        ResponseCustom<T> response = new ResponseCustom<>();
        response.setTimestamp(LocalDateTime.now());
        response.setStatus(HttpStatus.OK.value());
        response.setMessage(message);
        response.setData(t);
        return ResponseEntity.ok(response);
    }

    public <T> ResponseEntity<ResponseCustom<T>> fail(String message) {
        ResponseCustom<T> response = new ResponseCustom<>();
        response.setTimestamp(LocalDateTime.now());
        response.setStatus(HttpStatus.NOT_FOUND.value());
        response.setMessage(message);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

    public <T> ResponseEntity<ResponseCustom<T>> unavailable(String message) {
        ResponseCustom<T> response = new ResponseCustom<>();
        response.setTimestamp(LocalDateTime.now());
        response.setStatus(HttpStatus.SERVICE_UNAVAILABLE.value());
        response.setMessage(message);
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(response);
    }

    public <T> ResponseEntity<ResponseCustom<T>> badRequest(String message) {
        ResponseCustom<T> response = new ResponseCustom<>();
        response.setTimestamp(LocalDateTime.now());
        response.setStatus(HttpStatus.BAD_REQUEST.value());
        response.setMessage(message);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    public <T> ResponseEntity<ResponseCustom<T>> error(String message) {
        ResponseCustom<T> response = new ResponseCustom<>();
        response.setTimestamp(LocalDateTime.now());
        response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
        response.setMessage(message);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }

    public <T> ResponseEntity<ResponseCustom<T>> accessDenied(String message) {
        ResponseCustom<T> response = new ResponseCustom<>();
        response.setTimestamp(LocalDateTime.now());
        response.setStatus(HttpStatus.FORBIDDEN.value());
        response.setMessage(message);
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response);
    }
}
