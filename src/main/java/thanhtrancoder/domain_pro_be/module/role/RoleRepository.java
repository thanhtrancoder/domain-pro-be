package thanhtrancoder.domain_pro_be.module.role;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface RoleRepository extends JpaRepository<RoleEntity, Long> {
    @Query(nativeQuery = true, value = "SELECT * FROM role WHERE is_deleted = false")
    Page<RoleEntity> findAllByIsDeletedFalse(Pageable pageable);
}
