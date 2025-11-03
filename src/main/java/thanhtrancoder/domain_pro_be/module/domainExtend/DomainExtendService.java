package thanhtrancoder.domain_pro_be.module.domainExtend;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import thanhtrancoder.domain_pro_be.common.exceptions.CustomException;
import thanhtrancoder.domain_pro_be.common.exceptions.QueryException;
import thanhtrancoder.domain_pro_be.common.utils.StringUtils;
import thanhtrancoder.domain_pro_be.module.domainExtend.dto.DomainExtendDto;
import thanhtrancoder.domain_pro_be.module.domainExtend.dto.DomainPriceQuery;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class DomainExtendService {
    @Autowired
    private DomainExtendRepository domainExtendRepository;
    @Autowired
    private ModelMapper modelMapper;

    @Transactional
    public DomainExtendEntity adminInsert(DomainExtendDto domainExtendDto, Long createdBy) {
        if (domainExtendRepository.existsByNameAndIsDeleted(
                domainExtendDto.getName(),
                false)
        ) {
            throw new CustomException("Domain extension name already exists.");
        }
        try {
            DomainExtendEntity domainExtend = modelMapper.map(
                    domainExtendDto,
                    DomainExtendEntity.class
            );
            domainExtend.setCreatedAt(LocalDateTime.now());
            domainExtend.setCreatedBy(createdBy);
            domainExtend.setIsDeleted(false);
            return domainExtendRepository.save(domainExtend);
        } catch (Exception e) {
            throw new QueryException("An error occurred while creating the domain extension.", e);
        }
    }

    public Page<DomainExtendDto> searchAvailableDomainExtend(String domainName, Pageable pageable) {
        String domainNameWithoutAccent = StringUtils.removeAccent(domainName);
        String domainNameWithoutWhitespace = StringUtils.removeAllWhitespace(domainNameWithoutAccent);
        String domainNameWithoutSpecialCharacters = StringUtils.removeSpecialCharacters(domainNameWithoutWhitespace);
        String[] domainNameSplit = domainNameWithoutSpecialCharacters.trim().split("\\.");
        String domainOnlyName = domainNameSplit[0];
        if (domainNameSplit.length > 1) {
            domainOnlyName = domainNameSplit[domainNameSplit.length - 2];
        }
   
        Page<DomainExtendEntity> domainExtendEntities = domainExtendRepository
                .findAvailableDomainExtend(
                        domainOnlyName,
                        pageable
                );
        return domainExtendEntities.map(domainExtendEntity -> modelMapper.map(
                domainExtendEntity,
                DomainExtendDto.class)
        );
    }

    public DomainPriceQuery getPrice(Long domainExtendId, Integer period) {
        return domainExtendRepository.getPrice(domainExtendId, period);
    }

    public List<DomainExtendDto> getPopularDomainExtend() {
        List<DomainExtendEntity> query = domainExtendRepository.getPopularDomainExtend();
        return query.stream().map(domainExtendEntity -> modelMapper.map(
                domainExtendEntity,
                DomainExtendDto.class)
        ).collect(Collectors.toList());
    }
}

