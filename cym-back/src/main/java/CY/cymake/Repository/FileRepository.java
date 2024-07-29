package CY.cymake.Repository;

import CY.cymake.Entity.CompanyEntity;
import CY.cymake.Entity.FileEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.io.File;
import java.util.List;
import java.util.Optional;

public interface FileRepository extends JpaRepository<FileEntity, Long> {
    Optional<FileEntity> findByFile(String filename);
    List<FileEntity> findAllByCompanyCode(CompanyEntity companyCode);
    Optional<FileEntity> findByCompanyCodeAndFile(CompanyEntity companyCode, String file);
}
