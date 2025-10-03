package thanhtrancoder.domain_pro_be.module.passwordResets;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/password-resets")
public class PasswordResetsController {
    @Autowired
    private PasswordResetsService passwordResetsService;
}
