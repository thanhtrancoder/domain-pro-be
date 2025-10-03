package thanhtrancoder.domain_pro_be.module.passwordResets;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PasswordResetsService {
    @Autowired
    private PasswordResetsRepository passwordResetsRepository;
}
