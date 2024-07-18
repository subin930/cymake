package CY.cymake.Repository;

import CY.cymake.Entity.FileEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FileRepository extends JpaRepository<FileEntity, Long> {
    Optional<FileEntity> findByFile(String filename);
}
