package thanhtrancoder.domain_pro_be.module.notification;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import thanhtrancoder.domain_pro_be.module.notification.dto.NotificationDto;

@Service
public class NotificationService {
    @Autowired
    private NotificationRepository notificationRepository;
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
}
