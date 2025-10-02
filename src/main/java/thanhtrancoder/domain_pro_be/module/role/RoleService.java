package thanhtrancoder.domain_pro_be.module.role;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class RoleService {
    @Autowired
    private RoleRepository roleRepository;

    public Page<RoleEntity> getAll(Pageable pageable) {
        return roleRepository.findAllByIsDeletedFalse(pageable);
    }

    public RoleEntity create(RoleEntity roleEntity) {
        roleEntity.setCreatedAt(LocalDateTime.now());
        roleEntity.setCreatedBy(0L);
        roleEntity.setIsDeleted(false);
        return roleRepository.save(roleEntity);
    }
}
