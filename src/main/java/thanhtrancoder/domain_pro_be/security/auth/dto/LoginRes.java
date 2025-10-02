package thanhtrancoder.domain_pro_be.security.auth.dto;

import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginRes {
    private String token;
    private String fullname;
    private String email;
}
