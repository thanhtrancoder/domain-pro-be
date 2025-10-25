package thanhtrancoder.domain_pro_be.module.dnsConfig;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import thanhtrancoder.domain_pro_be.common.exceptions.CustomException;
import thanhtrancoder.domain_pro_be.common.exceptions.QueryException;
import thanhtrancoder.domain_pro_be.module.dnsConfig.dto.DnsConfigDto;
import thanhtrancoder.domain_pro_be.module.dnsConfig.dto.DnsConfigMatchReq;
import thanhtrancoder.domain_pro_be.module.domainName.DomainNameService;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class DnsConfigService {
    @Autowired
    private DnsConfigRepository dnsConfigRepository;
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private DomainNameService domainNameService;

    private String getDnsConfigString(DnsConfigDto dnsConfigDto) {
        return ""
                + dnsConfigDto.getType() + "-"
                + dnsConfigDto.getHost() + "-"
                + dnsConfigDto.getValue() + "-"
                + dnsConfigDto.getTtl();
    }

    public Page<DnsConfigDto> getAll(Long accountId, Long domainNameId, Pageable pageable) {
        domainNameService.checkExists(accountId, domainNameId);

        Page<DnsConfigEntity> dnsConfigEntities = dnsConfigRepository.
                findAllByDomainNameIdAndIsDeleted(
                        domainNameId,
                        false,
                        pageable
                );
        return dnsConfigEntities.map(
                dnsConfigEntity -> modelMapper.map(dnsConfigEntity, DnsConfigDto.class)
        );
    }

    @Transactional
    public DnsConfigDto create(Long accountId, DnsConfigDto dnsConfigDto) {
        domainNameService.checkExists(accountId, dnsConfigDto.getDomainNameId());
        if (dnsConfigDto.getType() == null || dnsConfigDto.getType().isEmpty()) {
            throw new CustomException("Loại cấu hình không hợp lệ: " + getDnsConfigString(dnsConfigDto));
        }
        if (dnsConfigDto.getHost() == null || dnsConfigDto.getHost().isEmpty()) {
            throw new CustomException("Máy chủ không được bỏ trống: " + getDnsConfigString(dnsConfigDto));
        }
        if (dnsConfigDto.getValue() == null || dnsConfigDto.getValue().isEmpty()) {
            throw new CustomException("Giá trị không được bỏ trống: " + getDnsConfigString(dnsConfigDto));
        }
        if (dnsConfigDto.getTtl() == null || dnsConfigDto.getTtl() < 0) {
            dnsConfigDto.setTtl(3600);
        }
        if (dnsConfigRepository.existsByDomainNameIdAndHostAndTypeAndIsDeleted(
                dnsConfigDto.getDomainNameId(),
                dnsConfigDto.getHost(),
                DnsType.valueOf(dnsConfigDto.getType()),
                false
        )) {
            throw new CustomException("Cấu hình DNS đã tồn tại: " + getDnsConfigString(dnsConfigDto));
        }

        try {
            DnsConfigEntity dnsConfigEntity = modelMapper.map(dnsConfigDto, DnsConfigEntity.class);
            dnsConfigEntity.setIsDeleted(false);
            dnsConfigEntity.setCreatedAt(LocalDateTime.now());
            dnsConfigEntity.setCreatedBy(accountId);
            dnsConfigRepository.save(dnsConfigEntity);
            return modelMapper.map(dnsConfigEntity, DnsConfigDto.class);
        } catch (Exception e) {
            throw new QueryException("Có lỗi xảy ra khi tạo cấu hình DNS.", e);
        }
    }

    @Transactional
    public DnsConfigDto update(Long accountId, DnsConfigDto dnsConfigDto) {
        domainNameService.checkExists(accountId, dnsConfigDto.getDomainNameId());
        if (dnsConfigDto.getType() == null || dnsConfigDto.getType().isEmpty()) {
            throw new CustomException("Loại cấu hình không hợp lệ: " + getDnsConfigString(dnsConfigDto));
        }
        if (dnsConfigDto.getHost() == null || dnsConfigDto.getHost().isEmpty()) {
            throw new CustomException("Máy chủ không được bỏ trống: " + getDnsConfigString(dnsConfigDto));
        }
        if (dnsConfigDto.getValue() == null || dnsConfigDto.getValue().isEmpty()) {
            throw new CustomException("Giá trị không được bỏ trống: " + getDnsConfigString(dnsConfigDto));
        }
        if (dnsConfigDto.getTtl() == null || dnsConfigDto.getTtl() < 0) {
            dnsConfigDto.setTtl(3600);
        }

        DnsConfigEntity dnsConfigEntity = dnsConfigRepository.findOneByDnsConfigIdAndIsDeleted(
                dnsConfigDto.getDnsConfigId(),
                false
        );
        if (dnsConfigEntity == null) {
            throw new CustomException("Không tìm thấy thông tin cấu hình DNS.");
        }

        try {
            modelMapper.map(dnsConfigDto, dnsConfigEntity);
            dnsConfigEntity.setUpdatedAt(LocalDateTime.now());
            dnsConfigEntity.setUpdatedBy(accountId);
            dnsConfigRepository.save(dnsConfigEntity);
            return modelMapper.map(dnsConfigEntity, DnsConfigDto.class);
        } catch (Exception e) {
            throw new QueryException("Có lỗi xảy ra khi cập nhật cấu hình DNS.", e);
        }
    }

    @Transactional
    public void delete(Long accountId, Long dnsConfigId) {
        Integer checkAccount = dnsConfigRepository.checkAccount(accountId, dnsConfigId);
        if (checkAccount == 0) {
            throw new CustomException("Không tìm thấy thông tin cấu hình DNS.");
        }

        DnsConfigEntity dnsConfigEntity = dnsConfigRepository.findOneByDnsConfigIdAndIsDeleted(
                dnsConfigId,
                false
        );
        if (dnsConfigEntity == null) {
            throw new CustomException("Không tìm thấy thông tin cấu hình DNS.");
        }

        try {
            dnsConfigEntity.setIsDeleted(true);
            dnsConfigEntity.setUpdatedAt(LocalDateTime.now());
            dnsConfigEntity.setUpdatedBy(accountId);
            dnsConfigRepository.save(dnsConfigEntity);
        } catch (Exception e) {
            throw new QueryException("Có lỗi xảy ra khi xóa cấu hình DNS.", e);
        }
    }

    @Transactional
    public Page<DnsConfigDto> match(Long accountId, DnsConfigMatchReq req) {
        Long domainNameId = req.getDomainNameId();
        try {
            for (DnsConfigDto dnsConfig : req.getDnsConfigs()) {
                if (dnsConfig.getDnsConfigId() == null) {
                    create(accountId, dnsConfig);
                } else if (dnsConfig.getDomainNameId() != null) {
                    update(accountId, dnsConfig);
                } else {
                    delete(accountId, dnsConfig.getDnsConfigId());
                }
            }

            Page<DnsConfigDto> dnsConfigDtoPage = getAll(accountId, domainNameId, Pageable.unpaged());
            return dnsConfigDtoPage;
        } catch (Exception e) {
            if (e instanceof CustomException || e instanceof QueryException) {
                throw new QueryException(e.getMessage(), e);
            }
            throw new QueryException("Có lỗi xảy ra khi cập nhật cấu hình DNS.", e);
        }
    }
}
