package CY.cymake.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.util.List;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "tb_news_data")
public class NewsDataEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "news_data_pk")
    private Long pk;

    @OneToOne
    @JoinColumn(name = "news_id", referencedColumnName = "news_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private CrwlNewsEntity id;

    @ElementCollection
    @CollectionTable(name = "news_summary", joinColumns = @JoinColumn(name = "news_data_pk"))
    @Column(name = "summary", nullable = false, length = 500)
    private List<String> summary;

    @ElementCollection
    @CollectionTable(name = "news_keywords", joinColumns = @JoinColumn(name = "news_data_pk"))
    @Column(name = "keywords", nullable = false, length = 500)
    private List<String> keywords;
}