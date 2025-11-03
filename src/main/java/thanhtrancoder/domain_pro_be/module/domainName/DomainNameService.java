package thanhtrancoder.domain_pro_be.module.domainName;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import thanhtrancoder.domain_pro_be.common.exceptions.CustomException;
import thanhtrancoder.domain_pro_be.common.exceptions.QueryException;
import thanhtrancoder.domain_pro_be.module.domainName.dto.DomainNameDashboardRes;
import thanhtrancoder.domain_pro_be.module.domainName.dto.DomainNameDto;

import java.time.LocalDateTime;

@Service
public class DomainNameService {
    @Autowired
    private DomainNameRepository domainNameRepository;
    @Autowired
    private ModelMapper modelMapper;

    public Boolean isAvailableDomain(String domainName, Long domainExtendId) {
        if (domainNameRepository.isAvailableDomain(domainName, domainExtendId) == 1) {
            return true;
        }
        return false;
    }

    public DomainNameDashboardRes getCount(Long accountId) {
        return modelMapper.map(
                domainNameRepository.getCount(accountId),
                DomainNameDashboardRes.class
        );
    }

    public Page<DomainNameDto> search(Long accountId, String keyword, Integer status, Pageable pageable) {
        Page<DomainNameEntity> domainNameList = domainNameRepository.search(
                accountId,
                keyword,
                status,
                pageable
        );

        return domainNameList.map(domainNameEntity -> modelMapper.map(
                domainNameEntity,
                DomainNameDto.class
        ));
    }

    @Transactional
    public DomainNameDto update(Long accountId, DomainNameDto domainNameDto) {
        DomainNameEntity domainNameEntity = domainNameRepository.
                findOneByDomainNameIdAndAccountIdAndIsDeleted(
                        domainNameDto.getDomainNameId(),
                        accountId,
                        false
                );
        if (domainNameEntity == null) {
            throw new RuntimeException("Domain does not exist.");
        }
        if (domainNameDto.getIsAutoRenewal() == domainNameEntity.getIsAutoRenewal()
                && domainNameDto.getIsBlock() == domainNameEntity.getIsBlock()) {
            throw new CustomException("Domain has no changes.");
        }

        try {
            domainNameEntity.setIsAutoRenewal(domainNameDto.getIsAutoRenewal());
            domainNameEntity.setIsBlock(domainNameDto.getIsBlock());
            domainNameEntity.setUpdatedAt(LocalDateTime.now());
            domainNameEntity.setUpdatedBy(accountId);
            domainNameRepository.save(domainNameEntity);
        } catch (Exception e) {
            throw new QueryException("An error occurred while updating the domain.", e);
        }

        return modelMapper.map(
                domainNameEntity,
                DomainNameDto.class
        );
    }

    public DomainNameDto getDetail(Long accountId, Long domainNameId) {
        DomainNameEntity domainNameEntity = domainNameRepository.getDetail(
                domainNameId,
                accountId
        );
        if (domainNameEntity == null) {
            throw new CustomException("Domain information not found.");
        }

        return modelMapper.map(
                domainNameEntity,
                DomainNameDto.class
        );
    }

    public void checkExists(Long accountId, Long domainNameId) {
        if (!domainNameRepository.existsByDomainNameIdAndAccountIdAndIsDeleted(
                domainNameId,
                accountId,
                false
        )) {
            throw new CustomException("Domain information not found.");
        }
    }

    @Transactional
    public void create(DomainNameDto domainNameDto, Long accountId) {
        try {
            DomainNameEntity domainNameEntity = modelMapper.map(
                    domainNameDto,
                    DomainNameEntity.class
            );
            domainNameEntity.setStatus(1);
            domainNameEntity.setAccountId(accountId);
            domainNameEntity.setCreatedAt(LocalDateTime.now());
            domainNameEntity.setCreatedBy(accountId);
            domainNameEntity.setIsDeleted(false);
            domainNameRepository.save(domainNameEntity);
        } catch (Exception e) {
            throw new QueryException("An error occurred while creating the domain.", e);
        }
    }
}

