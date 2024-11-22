package CY.cymake.Entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Entity
@Table(name = "tb_users")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UsersEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_pk")
    private Long pk;

    @ManyToOne
    @JoinColumn(name = "companyCode", referencedColumnName = "code")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private CompanyEntity companyCode;

    @Column(name = "id", unique = true, nullable = false)
    private String id;

    @Column(name = "username", nullable = false)
    private String username;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "email", unique = true, nullable = false, length = 500)
    @Email
    private String email;

    @Column(name = "role", nullable = false)
    @Enumerated(EnumType.STRING)
    private Role role;

    public void updatePWProfile(String password) {
        this.password = password;
    }
    public void updateProfile(String email) {
        this.email = email;
    }
}
