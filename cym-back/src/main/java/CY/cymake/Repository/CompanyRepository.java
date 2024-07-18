package CY.cymake.Repository;

import CY.cymake.Entity.CompanyEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CompanyRepository extends JpaRepository<CompanyEntity,Long> {
    Optional<CompanyEntity> findByCode(String code);
}
