package thanhtrancoder.domain_pro_be.module.notification;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import thanhtrancoder.domain_pro_be.common.exceptions.QueryException;
import thanhtrancoder.domain_pro_be.module.email.EmailService;
import thanhtrancoder.domain_pro_be.module.notification.dto.NotificationDto;
import thanhtrancoder.domain_pro_be.module.notification.dto.NotificationNewsReq;

import java.time.LocalDateTime;

@Service
public class NotificationService {
    @Autowired
    private NotificationRepository notificationRepository;
    @Autowired
    private EmailService emailService;
    @Autowired
    private ModelMapper modelMapper;

    public Page<NotificationDto> getAll(Long accountId, Pageable pageable) {
        Page<NotificationEntity> notificationList = notificationRepository
                .findAllByAccountIdAndIsDeleted(
                        accountId,
                        false,
                        pageable
                );
        return notificationList.map(notificationEntity -> modelMapper.map(
                notificationEntity,
                NotificationDto.class
        ));
    }

    @Transactional
    public void systemCreate(Long to, NotificationDto notificationDto) {
        try {
            NotificationEntity notificationEntity = modelMapper.map(
                    notificationDto,
                    NotificationEntity.class
            );
            notificationEntity.setAccountId(to);
            notificationEntity.setCreatedAt(LocalDateTime.now());
            notificationEntity.setCreatedBy(0L);
            notificationEntity.setIsDeleted(false);
            notificationRepository.save(notificationEntity);
        } catch (Exception e) {
            throw new QueryException("An error occurred while creating notification.", e);
        }
    }

    public void registerNews(NotificationNewsReq notificationNewsReq) {
        emailService.registrationNewsSuccess(notificationNewsReq.getEmail());
    }
}
