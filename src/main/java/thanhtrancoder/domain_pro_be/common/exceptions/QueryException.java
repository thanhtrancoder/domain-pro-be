package thanhtrancoder.domain_pro_be.common.exceptions;

public class QueryException extends RuntimeException {
    public QueryException(String message, Throwable cause) {
        super(message, cause);
    }
}
