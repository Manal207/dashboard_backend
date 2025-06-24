package tech.webapp.opticsmanager.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tech.webapp.opticsmanager.model.Role;
import tech.webapp.opticsmanager.model.User;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepo extends JpaRepository<User, Long> {

    /**
     * Find user by username
     */
    Optional<User> findByUsername(String username);

    /**
     * Find user by email
     */
    Optional<User> findByEmail(String email);

    /**
     * Check if user exists by username
     */
    boolean existsByUsername(String username);

    /**
     * Check if user exists by email
     */
    boolean existsByEmail(String email);

    /**
     * Find users by role
     */
    List<User> findByRole(Role role);

    /**
     * Find users by enabled status
     */
    List<User> findByEnabled(boolean enabled);

    /**
     * Find users by role and enabled status
     */
    List<User> findByRoleAndEnabled(Role role, boolean enabled);

    /**
     * Count users by role
     */
    long countByRole(Role role);

    /**
     * Legacy methods for backward compatibility (if needed)
     * These delegate to the standard JPA methods
     */
    default Optional<User> findUserById(Long id) {
        return findById(id);
    }

    default Optional<User> findUserByUsername(String username) {
        return findByUsername(username);
    }

    default void deleteUserById(Long id) {
        deleteById(id);
    }
}

//package tech.webapp.opticsmanager.repo;
//
//import org.springframework.data.jpa.repository.JpaRepository;
//import tech.webapp.opticsmanager.model.User;
//
//import java.util.List;
//import java.util.Optional;
//
//public interface UserRepo extends JpaRepository<User, Long> {
//    void deleteUserById(Long id);
//    Optional <User> findUserById(Long id);
//    Optional <User> findUserByEmail(String email);
//    Optional <User> findUserByUsername(String username);
//
////    Optional<User> findByUsername(String username);
//    Boolean existsByUsername(String username);
//    Boolean existsByEmail(String email);
//
//}
