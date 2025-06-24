package tech.webapp.opticsmanager.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.webapp.opticsmanager.exception.UserNotFoundException;
import tech.webapp.opticsmanager.model.Role;
import tech.webapp.opticsmanager.model.User;
import tech.webapp.opticsmanager.repo.UserRepo;

import java.util.List;

@Service
@Transactional
public class UserService {
    private final UserRepo userRepo;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepo userRepo, PasswordEncoder passwordEncoder) {
        this.userRepo = userRepo;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Create a new user with encoded password
     */
    public User createUser(User user) {
        // Encode password before saving
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        // Set default role if not specified
        if (user.getRole() == null) {
            user.setRole(Role.USER);
        }

        // Set default enabled status
        if (user.isEnabled() == false && user.getRole() != Role.ADMIN) {
            user.setEnabled(true);
        }

        return userRepo.save(user);
    }

    /**
     * Add user (legacy method for backward compatibility)
     */
    public User addUser(User user) {
        return createUser(user);
    }

    /**
     * Find all users
     */
    public List<User> findAllUsers() {
        return userRepo.findAll();
    }

    /**
     * Update existing user
     */
    public User updateUser(User user) {
        // Don't re-encode password if it's already encoded
        // This check prevents double encoding during updates
        User existingUser = findUserById(user.getId());

        if (user.getPassword() != null && !user.getPassword().equals(existingUser.getPassword())) {
            // Only encode if password is actually changed
            if (!user.getPassword().startsWith("$2a$") && !user.getPassword().startsWith("$2b$")) {
                user.setPassword(passwordEncoder.encode(user.getPassword()));
            }
        } else {
            // Keep existing password if not provided
            user.setPassword(existingUser.getPassword());
        }

        return userRepo.save(user);
    }

    /**
     * Delete user by ID
     */
    public void deleteUser(Long id) {
        User user = findUserById(id);

        // Prevent deletion of admin users
        if (user.getRole() == Role.ADMIN) {
            throw new RuntimeException("Cannot delete admin user");
        }

        userRepo.deleteById(id);
    }

    /**
     * Find user by ID
     */
    public User findUserById(Long id) {
        return userRepo.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User by id " + id + " was not found"));
    }

    /**
     * Find user by username
     */
    public User findUserByUsername(String username) {
        return userRepo.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException("User with username " + username + " not found"));
    }

    /**
     * Check if username exists
     */
    public boolean existsByUsername(String username) {
        return userRepo.existsByUsername(username);
    }

    /**
     * Check if email exists
     */
    public boolean existsByEmail(String email) {
        return userRepo.existsByEmail(email);
    }

    /**
     * Check if provided password matches encoded password
     */
    public boolean checkPassword(String rawPassword, String encodedPassword) {
        return passwordEncoder.matches(rawPassword, encodedPassword);
    }

    /**
     * Find user by email
     */
    public User findUserByEmail(String email) {
        return userRepo.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User with email " + email + " not found"));
    }

    /**
     * Enable/Disable user
     */
    public User toggleUserStatus(Long id) {
        User user = findUserById(id);

        // Prevent disabling admin users
        if (user.getRole() == Role.ADMIN) {
            throw new RuntimeException("Cannot disable admin user");
        }

        user.setEnabled(!user.isEnabled());
        return userRepo.save(user);
    }

    /**
     * Count total users
     */
    public long countUsers() {
        return userRepo.count();
    }

    /**
     * Find users by role
     */
    public List<User> findUsersByRole(Role role) {
        return userRepo.findByRole(role);
    }
}


//package tech.webapp.opticsmanager.service;
//
//import com.fasterxml.jackson.core.Base64Variant;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//import tech.webapp.opticsmanager.exception.UserNotFoundException;
//import tech.webapp.opticsmanager.model.User;
//import tech.webapp.opticsmanager.repo.UserRepo;
//
//import java.util.List;
//import java.util.UUID;
//
//@Service
//public class UserService {
//    private final UserRepo userRepo;
//    private final PasswordEncoder passwordEncoder;
//
//
//    @Autowired
//    public UserService(UserRepo userRepo, PasswordEncoder passwordEncoder) {
//        this.userRepo = userRepo;
//        this.passwordEncoder = passwordEncoder;
//    }
//
//    public User addUser(User user){
//        user.setUserCode(UUID.randomUUID().toString());
//        return userRepo.save(user);
//    }
//
//    public List<User> findAllUsers(){
//        return userRepo.findAll();
//    }
//
//    public User updateUser(User user){
//        return userRepo.save(user);
//    }
//
//    @Transactional
//    public void deleteUser(Long id){
//        userRepo.deleteUserById(id);
//    }
//
//    public User findUserById(Long id){
//        return userRepo.findUserById(id)
//                .orElseThrow(()-> new UserNotFoundException("User by id " + id + " was not found"));
//    }
//
////    public User findUserByUsername(String username){
////        return userRepo.findUserByUsername(username).orElseThrow(()-> new UserNotFoundException("User by username " + username + " was not found"));
////    }
//
//
//
//    public User findUserByUsername(String username) {
//        return userRepo.findUserByUsername(username)
//                .orElseThrow(() -> new RuntimeException("User with username " + username + " not found"));
//    }
//
//    public boolean existsByUsername(String username) {
//        return userRepo.existsByUsername(username);
//    }
//
//    public boolean existsByEmail(String email) {
//        return userRepo.existsByEmail(email);
//    }
//
//    public User createUser(User user) {
//        // Encode password before saving
//        user.setPassword(passwordEncoder.encode(user.getPassword()));
//        user.setUserCode(UUID.randomUUID().toString());
//        return userRepo.save(user);
//    }
//
//    public boolean checkPassword(String rawPassword, String encodedPassword) {
//        return passwordEncoder.matches(rawPassword, encodedPassword);
//    }
//}
