package thanhtrancoder.domain_pro_be.module.domainExtend;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import thanhtrancoder.domain_pro_be.common.exceptions.CustomException;
import thanhtrancoder.domain_pro_be.module.domainExtend.dto.DomainExtendDto;

import java.time.LocalDateTime;

@Service
public class DomainExtendService {
    @Autowired
    private DomainExtendRepository domainExtendRepository;
    @Autowired
    private ModelMapper modelMapper;

    @Transactional
    public DomainExtendEntity adminInsert(DomainExtendDto domainExtendDto, Long createdBy) {
        if (domainExtendRepository.existsByNameAndIsDeleted(domainExtendDto.getName(), false)) {
            throw new CustomException("Tên domain extend đã tồn tại.");
        }
        try {
            DomainExtendEntity domainExtend = modelMapper.map(domainExtendDto, DomainExtendEntity.class);
            domainExtend.setCreatedAt(LocalDateTime.now());
            domainExtend.setCreatedBy(createdBy);
            domainExtend.setIsDeleted(false);
            return domainExtendRepository.save(domainExtend);
        } catch (Exception e) {
            throw new CustomException("Có lỗi xảy ra khi tạo domain extend.");
        }
    }

    public Page<DomainExtendDto> searchAvailableDomainExtend(String domainName, Pageable pageable) {
        String[] domainNameSplit = domainName.trim().split("\\.");
        String domainOnlyName = domainNameSplit[0];
        if (domainNameSplit.length > 1) {
            domainOnlyName = domainNameSplit[domainNameSplit.length - 2];
        }
        Page<DomainExtendEntity> domainExtendEntities = domainExtendRepository.findAvailableDomainExtend(domainOnlyName, pageable);
        return domainExtendEntities.map(domainExtendEntity -> modelMapper.map(domainExtendEntity, DomainExtendDto.class));
    }
}
