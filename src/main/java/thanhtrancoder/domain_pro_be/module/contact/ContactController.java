package thanhtrancoder.domain_pro_be.module.contact;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import thanhtrancoder.domain_pro_be.common.entity.ResponseCustom;
import thanhtrancoder.domain_pro_be.common.entity.ResponseCustomService;
import thanhtrancoder.domain_pro_be.module.contact.dto.ContactDto;

@RestController
@RequestMapping("/api/contact")
public class ContactController {
    @Autowired
    private ContactService contactService;
    @Autowired
    private ResponseCustomService res;

    @PostMapping("/create")
    public ResponseEntity<ResponseCustom<Object>> createContact(@RequestBody ContactDto contactDto) {
        contactService.createContact(contactDto);
        return res.success("Liên hệ thành công.", null);
    }
}
