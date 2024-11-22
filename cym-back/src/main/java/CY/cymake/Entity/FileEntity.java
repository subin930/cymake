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
    @JoinColumn(name = "companyCode", referencedColumnName = "code")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private CompanyEntity companyCode;

    @Column(name = "postTitle", nullable = false, length = 500)
    private String postTitle;

    @Column(name = "original_fn", nullable = false, length = 500)
    private String originalFn; // 원래 파일 이름 example.txt

    @Column(name = "s3_fn", nullable = false, length = 500)
    private String s3Fn; //랜덤으로 바뀐 파일 이름(s3에 올라가 있는)

    @Column(name = "fileUrl", nullable = false, length = 500)
    private String fileUrl;

    @ManyToOne
    @JoinColumn(name = "uploader", referencedColumnName = "id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private UsersEntity uploader; //id저장

    @Column(name = "uploadDate", nullable = false)
    private Timestamp uploadDate;

    @Column(name = "lastEditDate", nullable = false)
    private Timestamp lastEditDate;

    @Column(name = "type", nullable = false)
    private String type;

    @Column(name = "size", nullable = false)
    private Double size;

    public void updatePost(String postTitle, String originalFn, String s3Fn, String fileUrl, String type, Double size) {
        this.postTitle = postTitle;
        this.originalFn = originalFn;
        this.s3Fn = s3Fn;
        this.fileUrl = fileUrl;
        this.type = type;
        this.lastEditDate = Timestamp.valueOf(LocalDateTime.now());
        this.size = size;
    }

    public void updatePostTitle(String postTitle) {
        this.postTitle = postTitle;
        this.lastEditDate = Timestamp.valueOf(LocalDateTime.now());
    }
}
