package thanhtrancoder.domain_pro_be.module.role;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import thanhtrancoder.domain_pro_be.common.exceptions.CustomException;
import thanhtrancoder.domain_pro_be.common.exceptions.QueryException;

import java.time.LocalDateTime;

@Service
public class RoleService {
    @Autowired
    private RoleRepository roleRepository;

    public Page<RoleEntity> getAll(Pageable pageable) {
        return roleRepository.findAllByIsDeletedFalse(pageable);
    }

    @Transactional
    public RoleEntity create(RoleEntity roleEntity) {
        try {
            roleEntity.setCreatedAt(LocalDateTime.now());
            roleEntity.setCreatedBy(0L);
            roleEntity.setIsDeleted(false);
            return roleRepository.save(roleEntity);
        } catch (Exception e) {
            throw new QueryException("An error occurred while creating the role.", e);
        }

    }
}

