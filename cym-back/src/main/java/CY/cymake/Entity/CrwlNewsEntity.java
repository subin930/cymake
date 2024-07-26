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

    @Column(unique = true, name = "title", nullable = false)
    private String title;

    @Column(name = "url", nullable = false)
    private String url;

    @Column(name = "newsLink", nullable = false)
    private String newsLink;

    @Column(name = "imgUrl")
    private String imgUrl;

    @Column(name = "uploadDate", nullable = false)
    private Timestamp uploadDate;

    @Column(name = "subject", nullable = false)
    private String subject;

}
