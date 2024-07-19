package CY.cymake.Repository;

import CY.cymake.Entity.CrwlNewsEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CrwlNewsRepository extends JpaRepository<CrwlNewsEntity, Long> {
    List<CrwlNewsEntity> findBySubject(String subject);
    List<CrwlNewsEntity> findBySubjectOrderByUploadDateDesc(String subject);
}
