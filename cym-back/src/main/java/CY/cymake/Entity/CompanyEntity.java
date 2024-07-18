package CY.cymake.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "tb_company")
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CompanyEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "company_id")
    private Long id;

    @Column(name = "code", unique = true, nullable = false)
    private String code;

    @Column(name = "name", unique = true, nullable = false)
    private String name;
}
