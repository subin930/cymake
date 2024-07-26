    package CY.cymake.Entity;

    import jakarta.persistence.*;
    import lombok.AllArgsConstructor;
    import lombok.Builder;
    import lombok.Getter;
    import lombok.NoArgsConstructor;
    import org.hibernate.annotations.ColumnDefault;


    @Entity
    @Table(name = "tb_crwl_total")
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public class CrwlTotalEntity {
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        @Column(name = "crawling_id")
        private Long id;

        @Column(name = "subject", unique = true, nullable = false)
        private String subject;

        @Column(name = "date", nullable = false)
        private String date;

        @Column(name = "total")
        @ColumnDefault("0")
        private Long total;
    }
