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

    public EmailBody registrationNewsSuccess(String userName, String websiteName, String websiteUrl, String unsubscribeUrl) {
        userName = userName == null ? "" : userName;
        EmailBody emailBody = new EmailBody();
        emailBody.setSubject("DomainPro - Thank you for registering");
        emailBody.setBody(
                """
                        <!DOCTYPE html>
                        <html lang="vi">
                        <head>
                            <meta charset="UTF-8">
                            <meta name="viewport" content="width=device-width, initial-scale=1.0">
                            <title>Xác nhận đăng ký nhận tin</title>
                            <style>
                                body {
                                    font-family: Arial, sans-serif;
                                    background-color: #f4f4f4;
                                    margin: 0;
                                    padding: 0;
                                }
                                .container {
                                    max-width: 600px;
                                    margin: 20px auto;
                                    background-color: #ffffff;
                                    padding: 20px;
                                    border-radius: 8px;
                                    box-shadow: 0 0 10px rgba(0,0,0,0.1);
                                }
                                .header {
                                    text-align: center;
                                    padding-bottom: 20px;
                                    border-bottom: 1px solid #eeeeee;
                                }
                                .header h1 {
                                    color: #007bff;
                                    margin: 0;
                                }
                                .content {
                                    padding: 20px 0;
                                    color: #333333;
                                    line-height: 1.6;
                                }
                                .content h2 {
                                    color: #333333;
                                }
                                .content p {
                                    margin-bottom: 16px;
                                }
                                .cta-button {
                                    display: inline-block;
                                    background-color: #007bff;
                                    color: #ffffff;
                                    padding: 12px 25px;
                                    text-decoration: none;
                                    border-radius: 5px;
                                    font-weight: bold;
                                    margin: 20px 0;
                                }
                                .footer {
                                    text-align: center;
                                    padding-top: 20px;
                                    border-top: 1px solid #eeeeee;
                                    font-size: 12px;
                                    color: #777777;
                                }
                                .footer a {
                                    color: #777777;
                                    text-decoration: underline;
                                }
                            </style>
                        </head>
                        <body>
                            <div class="container">
                                <div class="header">
                                    <h1>Chào mừng đến với %s!</h1>
                                </div>
                                <div class="content">
                                    <h2>Cảm ơn bạn đã đăng ký!</h2>
                                    <p>Chào bạn %s,</p>
                                    <p>Bạn đã đăng ký thành công để nhận những thông tin mới nhất, các chương trình khuyến mãi hấp dẫn và những mẹo hữu ích về tên miền, hosting từ chúng tôi.</p>
                                    <p>Chúng tôi cam kết sẽ chỉ gửi những email thực sự giá trị và không làm phiền bạn.</p>
                                    <p>Hãy khám phá thêm các dịch vụ của chúng tôi:</p>
                                    <a href="%s" class="cta-button">Khám Phá Ngay</a>
                                    <p>Nếu bạn có bất kỳ câu hỏi nào, đừng ngần ngại trả lời email này nhé.</p>
                                    <p>Trân trọng,<br>Đội ngũ %s</p>
                                </div>
                                <div class="footer">
                                    <p>&copy; 2024 %s. All rights reserved.</p>
                                </div>
                            </div>
                        </body>
                        </html>
                        """.formatted(websiteName, userName, websiteUrl, websiteName, websiteName, unsubscribeUrl)
        );
        return emailBody;
    }
}
