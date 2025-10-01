package thanhtrancoder.domain_pro_be.accountRole;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import thanhtrancoder.domain_pro_be.base.BaseEntity;

@Entity
@Table(name = "account_role")
@IdClass(AccountRoleId.class)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AccountRoleEntity extends BaseEntity {
    @Id
    @Column(name = "account_id", nullable = false)
    private Long accountId;

    @Id
    @Column(name = "role_id", nullable = false)
    private Long roleId;
}
