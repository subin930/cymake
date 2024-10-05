package CY.cymake.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;

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

    @Column(name = "code", unique = true, nullable = false, length = 500)
    private String code;

    @Column(name = "name", unique = true, nullable = false, length = 500)
    private String name;

    @Column(name = "plan", nullable = false)
    @ColumnDefault("'basic'")
    private String plan;

    @Column(name = "current_usage", nullable = false)
    @ColumnDefault("0.0")
    private Double current_usage;

    public void uddUsage(double usage){
        this.current_usage += usage;
    }
    public void deleteUsage(double usage){
        this.current_usage -= usage;
    }
    public void updateUsage(double usage, double update_usage){
        this.current_usage -= usage;
        this.current_usage += update_usage;
    }
    public void changePlan(String plan){
        this.plan = plan;
    }
}
