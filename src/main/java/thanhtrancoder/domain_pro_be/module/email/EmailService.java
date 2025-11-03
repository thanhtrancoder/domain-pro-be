package thanhtrancoder.domain_pro_be.module.email;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import thanhtrancoder.domain_pro_be.common.exceptions.CustomException;
import thanhtrancoder.domain_pro_be.module.account.AccountEntity;
import thanhtrancoder.domain_pro_be.module.account.AccountService;
import thanhtrancoder.domain_pro_be.module.email.dto.EmailBody;

import java.util.Map;

@Service
public class EmailService {
    @Autowired
    private JavaMailSender mailSender;
    @Autowired
    private AccountService accountService;

    @Value("${frontend.origin}")
    private String frontendOrigin;

    public void sendSimpleEmail(String to, String subject, String text) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);
        mailSender.send(message);
    }

    private void sendHtmlEmail(String to, String subject, String htmlBody) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(htmlBody, true);  // true = html
            mailSender.send(message);
        } catch (MessagingException e) {
            throw new CustomException("Có lỗi xảy ra khi gửi email.");
        }
    }

    public void registrationSuccess(Long accountId) {
        AccountEntity account = accountService.getAccountAccountId(accountId);

        EmailTemplate emailTemplate = new EmailTemplate();
        EmailBody emailBody = emailTemplate.registrationSuccess(account.getFullname());
        sendHtmlEmail(account.getEmail(), emailBody.getSubject(), emailBody.getBody());
    }

    public void passwordChanged(Long accountId) {
        AccountEntity account = accountService.getAccountAccountId(accountId);

        EmailTemplate emailTemplate = new EmailTemplate();
        EmailBody emailBody = emailTemplate.passwordChanged(account.getFullname());
        sendHtmlEmail(account.getEmail(), emailBody.getSubject(), emailBody.getBody());
    }

    public void forgotPasswordOtp(Long accountId, String otp, int expireInMinutes) {
        AccountEntity account = accountService.getAccountAccountId(accountId);

        EmailTemplate emailTemplate = new EmailTemplate();
        EmailBody emailBody = emailTemplate.forgotPasswordOtp(account.getFullname(), otp, expireInMinutes);
        sendHtmlEmail(account.getEmail(), emailBody.getSubject(), emailBody.getBody());
    }

    public void orderPaymentSuccess(
            Long accountId,
            String orderId,
            Map<String, Integer> domainsWithYears
    ) {
        AccountEntity account = accountService.getAccountAccountId(accountId);

        EmailTemplate emailTemplate = new EmailTemplate();
        EmailBody emailBody = emailTemplate.orderPaymentSuccess(account.getFullname(), orderId, domainsWithYears);
        sendHtmlEmail(account.getEmail(), emailBody.getSubject(), emailBody.getBody());
    }

    public void registrationNewsSuccess(String email) {
        EmailTemplate emailTemplate = new EmailTemplate();
        EmailBody emailBody = emailTemplate.registrationNewsSuccess(
                email,
                "DomainPro",
                frontendOrigin,
                "https://domainpro.com/unsubscribe"
        );
        sendHtmlEmail(email, emailBody.getSubject(), emailBody.getBody());
    }
}
