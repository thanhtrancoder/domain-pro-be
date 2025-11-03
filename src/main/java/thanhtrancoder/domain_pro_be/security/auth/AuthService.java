package thanhtrancoder.domain_pro_be.security.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import thanhtrancoder.domain_pro_be.common.exceptions.QueryException;
import thanhtrancoder.domain_pro_be.common.utils.RegexUtils;
import thanhtrancoder.domain_pro_be.module.account.AccountEntity;
import thanhtrancoder.domain_pro_be.module.account.AccountService;
import thanhtrancoder.domain_pro_be.module.account.dto.AccountProfileRes;
import thanhtrancoder.domain_pro_be.module.account.dto.AccountUpdateReq;
import thanhtrancoder.domain_pro_be.module.email.EmailService;
import thanhtrancoder.domain_pro_be.module.passwordResets.PasswordResetsService;
import thanhtrancoder.domain_pro_be.security.auth.dto.LoginReq;
import thanhtrancoder.domain_pro_be.security.auth.dto.RegisterReq;
import thanhtrancoder.domain_pro_be.common.exceptions.CustomException;
import thanhtrancoder.domain_pro_be.security.auth.dto.ResetPasswordReq;
import thanhtrancoder.domain_pro_be.security.auth.dto.UpdateReq;

@Service
public class AuthService {
    @Autowired
    private AccountService accountService;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private EmailService emailService;
    @Autowired
    private PasswordResetsService passwordResetsService;

    public AccountEntity login(LoginReq loginReq) {
        AccountEntity account = accountService.loginByEmail(loginReq.getEmail());
        if (account == null) {
            throw new CustomException("Incorrect username or password");
        }
        if (!passwordEncoder.matches(loginReq.getPassword(), account.getPassword())) {
            throw new CustomException("Incorrect username or password");
        }
        if (account.getPassword() == null) {
            throw new CustomException("Incorrect username or password");
        }
        return account;
    }

    @Transactional
    public void register(RegisterReq registerReq) {
        if (registerReq.getEmail() == null
                || registerReq.getEmail().trim().isEmpty()
                || !RegexUtils.EMAIL_REGEX.matcher(registerReq.getEmail().trim()).matches()) {
            throw new CustomException("Invalid email.");
        }
        if (!registerReq.getPassword().equals(registerReq.getConfirmPassword())) {
            throw new CustomException("Passwords do not match");
        }
        if (registerReq.getPassword().length() < 8) {
            throw new CustomException("Password must be at least 8 characters");
        }
        if (registerReq.getPassword().toLowerCase().equals(registerReq.getPassword())) {
            throw new CustomException("Password must contain an uppercase letter");
        }
        if (registerReq.getPassword().toUpperCase().equals(registerReq.getPassword())) {
            throw new CustomException("Password must contain a lowercase letter");
        }
        if (!registerReq.getPassword().matches(".*[0-9].*")) {
            throw new CustomException("Password must contain a number");
        }
        if (!registerReq.getPassword().matches(".*[!@#$%^&*()].*")) {
            throw new CustomException("Password must contain a special character");
        }

        try {
            AccountEntity account = new AccountEntity();
            account.setEmail(registerReq.getEmail());
            account.setPassword(passwordEncoder.encode(registerReq.getPassword()));
            account.setIsVerify(false);
            account.setTokenVersion(1);
            accountService.create(account);

            // Send email
            emailService.registrationSuccess(account.getAccountId());
        } catch (Exception e) {
            if (e instanceof CustomException) {
                throw new CustomException(e.getMessage());
            }
            throw new QueryException("An error occurred during registration.", e);
        }
    }

    public AccountEntity getCurrentAccount() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.isAuthenticated()) {
            Object principal = auth.getPrincipal();
            if (principal instanceof UserDetails) {
                UserDetails user = (UserDetails) principal;
                String email = user.getUsername();
                return accountService.getActiveAccountByEmail(email);
            }
        }
        return null;
    }

    public Long getCurrentAccountId() {
        return getCurrentAccount().getAccountId();
    }

    @Transactional
    public AccountProfileRes updateAccount(UpdateReq updateReq) {
        AccountEntity account = getCurrentAccount();
        if (account == null) {
            throw new CustomException("Account does not exist");
        }

        AccountUpdateReq accountUpdateReq = new AccountUpdateReq();

        if (!updateReq.getFullname().equals(account.getFullname())) {
            accountUpdateReq.setFullname(updateReq.getFullname().trim());
        }

        if (updateReq.getOldPassword() != null && !updateReq.getOldPassword().isEmpty()) {
            if (!passwordEncoder.matches(updateReq.getOldPassword(), account.getPassword())) {
                throw new CustomException("Old password is incorrect");
            }
            if (!updateReq.getNewPassword().equals(updateReq.getConfirmPassword())) {
                throw new CustomException("New password and confirmation do not match.");
            }
            if (updateReq.getNewPassword().length() < 8) {
                throw new CustomException("New password must be at least 8 characters");
            }
            if (updateReq.getNewPassword().toLowerCase().equals(updateReq.getNewPassword())) {
                throw new CustomException("New password must contain an uppercase letter");
            }
            if (updateReq.getNewPassword().toUpperCase().equals(updateReq.getNewPassword())) {
                throw new CustomException("New password must contain a lowercase letter");
            }
            if (!updateReq.getNewPassword().matches(".*[0-9].*")) {
                throw new CustomException("New password must contain a number");
            }
            if (!updateReq.getNewPassword().matches(".*[!@#$%^&*()].*")) {
                throw new CustomException("New password must contain a special character");
            }
            accountUpdateReq.setPasswordEncoded(passwordEncoder.encode(updateReq.getNewPassword()));
        }

        if (accountUpdateReq.getFullname() == null && accountUpdateReq.getPasswordEncoded() == null) {
            throw new CustomException("No information was changed.");
        }

        try {
            accountService.update(accountUpdateReq, account.getAccountId());

            if (accountUpdateReq.getPasswordEncoded() != null) {
                emailService.passwordChanged(account.getAccountId());
            }

            AccountProfileRes accountProfileRes = accountService.getProfileByAccount(account);

            return accountProfileRes;
        } catch (Exception e) {
            if (e instanceof CustomException) {
                throw new CustomException(e.getMessage());
            }
            throw new QueryException("An error occurred while updating the account.", e);
        }
    }

    @Transactional
    public void forgotPassword(String email) {
        if (accountService.checkActiveAccountByEmail(email)) {
            AccountEntity account = accountService.getActiveAccountByEmail(email);

            try {
                String otp = passwordResetsService.create(account.getAccountId());

                emailService.forgotPasswordOtp(
                        account.getAccountId(),
                        otp,
                        10
                );
            } catch (Exception e) {
                if (e instanceof CustomException) {
                    throw new CustomException(e.getMessage());
                }
                throw new QueryException("An error occurred while requesting a password reset", e);
            }
        } else {
            try {
                Thread.sleep(4000);  // 4 seconds = 4000 ms
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    @Transactional
    public Boolean resetPassword(ResetPasswordReq resetPasswordReq) {
        if (resetPasswordReq.getEmail() == null
                || resetPasswordReq.getEmail().trim().isEmpty()
                || !RegexUtils.EMAIL_REGEX.matcher(resetPasswordReq.getEmail().trim()).matches()) {
            throw new CustomException("Invalid email.");
        }
        if (resetPasswordReq.getOtp() == null
                || resetPasswordReq.getOtp().trim().isEmpty()
                || resetPasswordReq.getOtp().length() != 6) {
            throw new CustomException("Invalid OTP.");
        }
        if (!resetPasswordReq.getPassword().equals(resetPasswordReq.getConfirmPassword())) {
            throw new CustomException("New password and confirmation do not match.");
        }
        if (resetPasswordReq.getPassword().length() < 8) {
            throw new CustomException("New password must be at least 8 characters");
        }
        if (resetPasswordReq.getPassword().toLowerCase().equals(resetPasswordReq.getPassword())) {
            throw new CustomException("New password must contain an uppercase letter");
        }
        if (resetPasswordReq.getPassword().toUpperCase().equals(resetPasswordReq.getPassword())) {
            throw new CustomException("New password must contain a lowercase letter");
        }
        if (!resetPasswordReq.getPassword().matches(".*[0-9].*")) {
            throw new CustomException("New password must contain a number");
        }
        if (!resetPasswordReq.getPassword().matches(".*[!@#$%^&*()].*")) {
            throw new CustomException("New password must contain a special character");
        }

        if (accountService.checkActiveAccountByEmail(resetPasswordReq.getEmail())) {
            try {
                AccountEntity account = accountService.getActiveAccountByEmail(resetPasswordReq.getEmail());

                Boolean updateOtp = passwordResetsService.apply(account.getAccountId(), resetPasswordReq.getOtp());
                if (!updateOtp) {
                    return false;
                }

                AccountUpdateReq accountUpdateReq = new AccountUpdateReq();
                accountUpdateReq.setFullname(account.getFullname());
                accountUpdateReq.setPasswordEncoded(passwordEncoder.encode(resetPasswordReq.getPassword()));
                accountService.update(accountUpdateReq, account.getAccountId());

                emailService.passwordChanged(account.getAccountId());
                return true;
            } catch (Exception e) {
                if (e instanceof CustomException) {
                    throw new CustomException(e.getMessage());
                }
                throw new QueryException("An error occurred while resetting the password", e);
            }
        }
        return false;
    }
}
