package thanhtrancoder.domain_pro_be.security.auth.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegisterReq {
    private String email;
    private String password;
    private String confirmPassword;
}
