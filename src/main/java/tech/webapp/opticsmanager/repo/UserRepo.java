package tech.webapp.opticsmanager.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import tech.webapp.opticsmanager.model.User;

import java.util.List;
import java.util.Optional;

public interface UserRepo extends JpaRepository<User, Long> {
    void deleteUserById(Long id);
    Optional <User> findUserById(Long id);
    Optional <User> findUserByEmail(String email);
//    Optional <User> findUserByUsername(String username);

    Optional<User> findByUsername(String username);
    Boolean existsByUsername(String username);
    Boolean existsByEmail(String email);

}
