package CY.cymake.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

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

    @Column(unique = true, name = "title", nullable = false, length = 500)
    private String title;

    @Column(name = "url", nullable = false, length = 500)
    private String url;

    @Column(name = "newsLink", nullable = false, length = 500)
    private String newsLink;

    @Column(name = "imgUrl", length = 500)
    private String imgUrl;

    @Column(name = "uploadDate", nullable = false)
    private Timestamp uploadDate;

    @Column(name = "subject", nullable = false, length = 500)
    private String subject;

}
