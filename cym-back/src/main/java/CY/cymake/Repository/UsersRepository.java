package CY.cymake.Repository;

import CY.cymake.Entity.UsersEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UsersRepository extends JpaRepository<UsersEntity, Long> {
    Optional<UsersEntity> findById(String id);
    //Optional<UsersEntity> findByUsername(String username);
    Optional<UsersEntity> findByEmail(String email);
}
