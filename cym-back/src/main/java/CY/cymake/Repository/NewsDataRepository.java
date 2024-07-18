package CY.cymake.Repository;

import CY.cymake.Entity.NewsDataEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NewsDataRepository extends JpaRepository<NewsDataEntity, Long> {
}
