package thanhtrancoder.domain_pro_be.account;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import thanhtrancoder.domain_pro_be.base.BaseEntity;

@Entity
@Table(name = "account")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AccountEntity extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "account_id", nullable = false)
    private Long accountId;

    @Column(name = "fullname", length = 200)
    private String fullname;

    @Column(name = "email", nullable = false, length = 200, unique = true)
    private String email;

    @Column(name = "password", nullable = false, length = 255)
    private String password;

    @Column(name = "is_verify", nullable = false)
    private Boolean isVerify;

    @Column(name = "google_id", length = 200, unique = true)
    private String googleId;
}
