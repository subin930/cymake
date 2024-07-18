package CY.cymake.Entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.util.List;

@Entity
@Getter
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

    @Column(name = "summary", nullable = false)
    private String summary;

    @ElementCollection
    @Column(name = "keywords") // , nullable = false 추가하기 나중에
    private List<String> keywords; //나중에 배열로 저장해보기
}
