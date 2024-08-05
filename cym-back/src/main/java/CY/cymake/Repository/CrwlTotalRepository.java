package CY.cymake.Repository;

import CY.cymake.Entity.CrwlTotalEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CrwlTotalRepository extends JpaRepository<CrwlTotalEntity, Long> {
    List<CrwlTotalEntity> findBySubjectOrderByDate(String subject);
}
