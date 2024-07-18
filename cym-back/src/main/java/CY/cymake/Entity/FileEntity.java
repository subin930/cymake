package CY.cymake.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.sql.Timestamp;
import java.time.LocalDateTime;

@Entity
@Table(name = "tb_file")
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FileEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "file_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "company_code", referencedColumnName = "code")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private CompanyEntity company_code;

    @Column(name = "post_title", nullable = false)
    private String post_title;

    @Column(name = "file_name", nullable = false)
    private String file;

    @Column(name = "file_url", nullable = false)
    private String file_url;

    @ManyToOne
    @JoinColumn(name = "uploader", referencedColumnName = "id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private UsersEntity uploader; //id저장

    @Column(name = "upload_date", nullable = false)
    private Timestamp upload_date;

    @Column(name = "last_edit_date", nullable = false)
    private Timestamp last_edit_date;

    @Column(name = "type", nullable = false)
    private String type;

    public void updatePost(String post_title, String file_name, String file_url, String type) {
        this.post_title = post_title;
        this.file = file_name;
        this.file_url = file_url;
        this.type = type;
        this.last_edit_date = Timestamp.valueOf(LocalDateTime.now());
    }
}
