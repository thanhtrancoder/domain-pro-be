package thanhtrancoder.domain_pro_be.common.exceptions;

public class LoginSessionExpired extends RuntimeException {
    public LoginSessionExpired(String message) {
        super(message);
    }
}
