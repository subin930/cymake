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
    @JoinColumn(name = "company_code", referencedColumnName = "code")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private CompanyEntity company_code;

    @Column(name = "id", unique = true, nullable = false)
    private String id;

    @Column(name = "username", nullable = false)
    private String username;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "email", unique = true, nullable = false)
    @Email
    private String email;

    @Column(name = "role", nullable = false)
    @Enumerated(EnumType.STRING)
    private Role role;

    public void updateProfile(String password, String email) {
        this.password = password;
        this.email = email;
    }
}
