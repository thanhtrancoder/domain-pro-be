package thanhtrancoder.domain_pro_be.common.exceptions;

import lombok.Getter;

import java.util.Objects;

@Getter
public class ExternalException extends RuntimeException {
    private final Object data;

    public ExternalException(String message, Object data) {
        super(message);
        this.data = data;
    }
}
