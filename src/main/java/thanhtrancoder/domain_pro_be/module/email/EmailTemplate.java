package thanhtrancoder.domain_pro_be.module.email;

import thanhtrancoder.domain_pro_be.module.email.dto.EmailBody;

import java.util.Map;

public class EmailTemplate {
    public EmailBody registrationSuccess(String userName) {
        userName = userName == null ? "" : userName;
        EmailBody emailBody = new EmailBody();
        emailBody.setSubject("DomainPro - Thank you for registering");
        emailBody.setBody(
                "<!DOCTYPE html>"
                        + "<html lang=\"en\">"
                        + "<head><meta charset=\"UTF-8\"></head>"
                        + "<body>"
                        + "<p>Welcome <strong>" + userName + "</strong>,</p>"
                        + "<p>Thank you for registering with us.</p>"
                        + "<p>Upon logging in, you will be able to access other services including searching for your favourite domain, managing your domain, and editing your account information.</p>"
                        + "<br/>"
                        + "<p>Thanks,<br/>DomainPro</p>"
                        + "</body>"
                        + "</html>"
        );

        return emailBody;
    }

    public EmailBody passwordChanged(String userName) {
        userName = userName == null ? "" : userName;
        EmailBody emailBody = new EmailBody();
        emailBody.setSubject("DomainPro – Your password has been changed");
        emailBody.setBody(
                "<!DOCTYPE html>"
                        + "<html lang=\"en\">"
                        + "<head><meta charset=\"UTF-8\"></head>"
                        + "<body>"
                        + "<p>Hi <strong>" + userName + "</strong>,</p>"
                        + "<p>We want to let you know that your password was successfully changed.</p>"
                        + "<p>If you did not make this change, please contact our support team immediately.</p>"
                        + "<br/>"
                        + "<p>Thank you,<br/>DomainPro Team</p>"
                        + "</body>"
                        + "</html>"
        );
        return emailBody;
    }

    public EmailBody forgotPasswordOtp(String userName, String otpCode, int validMinutes) {
        userName = userName == null ? "" : userName;
        EmailBody emailBody = new EmailBody();
        emailBody.setSubject("DomainPro – Password Reset Code");
        emailBody.setBody(
                "<!DOCTYPE html>"
                        + "<html lang=\"en\">"
                        + "<head><meta charset=\"UTF-8\"></head>"
                        + "<body>"
                        + "<p>Hello <strong>" + userName + "</strong>,</p>"
                        + "<p>We received a request to reset your password for your DomainPro account.</p>"
                        + "<p>Your one-time code (OTP) is:</p>"
                        + "<h2 style=\"color:#2E86C1;\">" + otpCode + "</h2>"
                        + "<p>This code is valid for <strong>" + validMinutes + " minutes</strong>.</p>"
                        + "<p>If you did not request this, please ignore this email or contact our support.</p>"
                        + "<br/>"
                        + "<p>Thank you,<br/>DomainPro Team</p>"
                        + "</body>"
                        + "</html>"
        );
        return emailBody;
    }

    public EmailBody orderPaymentSuccess(String userName,
                                         String orderId,
                                         Map<String, Integer> domainsWithYears) {
        userName = userName == null ? "" : userName;
        EmailBody emailBody = new EmailBody();
        emailBody.setSubject("DomainPro – Your Order Payment was Successful");

        StringBuilder body = new StringBuilder();
        body.append("<!DOCTYPE html>")
                .append("<html lang=\"en\">")
                .append("<head><meta charset=\"UTF-8\"></head>")
                .append("<body>")
                .append("<p>Hi <strong>").append(userName).append("</strong>,</p>")
                .append("<p>Thank you for your payment. Your order <strong>#")
                .append(orderId).append("</strong> has been processed successfully.</p>")
                .append("<p>Here are the details of your domains:</p>")
                .append("<table style=\"border-collapse: collapse; width: auto; max-width: 100%; margin: 0 auto;\">")
                .append("<thead>")
                .append("<tr>")
                .append("<th style=\"border: 1px solid #dddddd; text-align: left; padding: 8px;\">Domain</th>")
                .append("<th style=\"border: 1px solid #dddddd; text-align: left; padding: 8px;\">Duration (years)</th>")
                .append("</tr>")
                .append("</thead>")
                .append("<tbody>");

        for (Map.Entry<String, Integer> entry : domainsWithYears.entrySet()) {
            String domain = entry.getKey();
            Integer years = entry.getValue();
            body.append("<tr>")
                    .append("<td style=\"border: 1px solid #dddddd; padding: 8px;\">")
                    .append(domain)
                    .append("</td>")
                    .append("<td style=\"border: 1px solid #dddddd; padding: 8px;\">")
                    .append(years)
                    .append("</td>")
                    .append("</tr>");
        }

        body.append("</tbody>")
                .append("</table>")
                .append("<p>You may now log in to your account and manage your domains at any time.</p>")
                .append("<br/>")
                .append("<p>Thanks,<br/>DomainPro Team</p>")
                .append("</body>")
                .append("</html>");

        emailBody.setBody(body.toString());
        return emailBody;
    }
}
