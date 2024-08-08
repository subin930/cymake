package CY.cymake.Repository;

import CY.cymake.Entity.CompanyEntity;
import CY.cymake.Entity.FileEntity;
import CY.cymake.Entity.UsersEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FileRepository extends JpaRepository<FileEntity, Long> {
    List<FileEntity> findAllByCompanyCode(CompanyEntity companyCode);
    Optional<FileEntity> findByCompanyCodeAndId(CompanyEntity companyCode, Long id);
    List<FileEntity> findAllByUploader(UsersEntity uploader);
}
