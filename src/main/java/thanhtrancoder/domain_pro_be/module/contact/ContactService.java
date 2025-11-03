package thanhtrancoder.domain_pro_be.module.contact;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import thanhtrancoder.domain_pro_be.common.exceptions.CustomException;
import thanhtrancoder.domain_pro_be.common.utils.RegexUtils;
import thanhtrancoder.domain_pro_be.module.contact.dto.ContactDto;
import thanhtrancoder.domain_pro_be.module.email.EmailService;

@Service
public class ContactService {
    @Autowired
    private EmailService emailService;

    public void createContact(ContactDto contactDto) {
        if (contactDto.getName() == null || contactDto.getName().trim().isEmpty()) {
            throw new CustomException("Tên không thể bỏ trống.");
        }
        if (contactDto.getEmail() == null
                || contactDto.getEmail().trim().isEmpty()
                || !RegexUtils.EMAIL_REGEX.matcher(contactDto.getEmail().trim()).matches()) {
            throw new CustomException("Email không hợp lệ.");
        }
        if (contactDto.getMessage() == null || contactDto.getMessage().trim().isEmpty()) {
            throw new CustomException("Nội dung không thể bỏ trống.");
        }

        emailService.sendSimpleEmail(
                "thanhtrancoder@gmail.com",
                "Contact from " + contactDto.getName(),
                contactDto.getMessage() + "\n\n" + contactDto.getEmail()
        );
    }
}
