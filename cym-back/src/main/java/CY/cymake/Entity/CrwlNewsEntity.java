package CY.cymake.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.sql.Timestamp;
import java.util.List;

@Table(name = "tb_crwl_news")
@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CrwlNewsEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "news_id")
    private Long id;

    @Column(unique = true, name = "title", nullable = false)
    private String title;

    @Column(name = "url", nullable = false)
    private String url;

    @Column(name = "news_link", nullable = false)
    private String news_link;

    @Column(name = "img_url")
    private String img_url;

    @Column(name = "upload_date", nullable = false)
    private Timestamp upload_date;

    @Column(name = "subject", nullable = false)
    private String subject;

}
