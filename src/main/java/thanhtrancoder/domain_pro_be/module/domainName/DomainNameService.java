package thanhtrancoder.domain_pro_be.module.domainName;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import thanhtrancoder.domain_pro_be.module.domainName.dto.DomainNameDto;

@Service
public class DomainNameService {
    @Autowired
    private DomainNameRepository domainNameRepository;
}
