package tech.webapp.opticsmanager.resource;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import tech.webapp.opticsmanager.dto.auth.CreateUserDTO;
import tech.webapp.opticsmanager.model.Role;
import tech.webapp.opticsmanager.model.User;
import tech.webapp.opticsmanager.service.UserService;

import jakarta.validation.Valid;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admin")
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {

    private final UserService userService;

    public AdminController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/test")
    public ResponseEntity<?> testAdmin() {
        return ResponseEntity.ok(Map.of("message", "Admin endpoint works!"));
    }

    @GetMapping("/users")
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users = userService.findAllUsers();
        return ResponseEntity.ok(users);
    }

    @PostMapping("/users")
    public ResponseEntity<?> createUser(@Valid @RequestBody CreateUserDTO createUserDTO) {
        try {
            // Check if username already exists
            if (userService.existsByUsername(createUserDTO.getUsername())) {
                return ResponseEntity.badRequest()
                        .body(Map.of("message", "Username is already taken"));
            }

            // Check if email already exists
            if (userService.existsByEmail(createUserDTO.getEmail())) {
                return ResponseEntity.badRequest()
                        .body(Map.of("message", "Email is already in use"));
            }

            User user = new User();
            user.setUsername(createUserDTO.getUsername());
            user.setPassword(createUserDTO.getPassword()); // Will be encoded in service
            user.setEmail(createUserDTO.getEmail());
            user.setName(createUserDTO.getName());
            user.setRole(Role.USER); // Default role for new users

            User createdUser = userService.createUser(user);

            // Remove password from response
            createdUser.setPassword(null);

            return ResponseEntity.status(HttpStatus.CREATED).body(createdUser);
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(Map.of("message", "Error creating user: " + e.getMessage()));
        }
    }

    @DeleteMapping("/users/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable Long id) {
        try {
            User user = userService.findUserById(id);

            // Prevent admin from deleting themselves
            if (user.getRole() == Role.ADMIN) {
                return ResponseEntity.badRequest()
                        .body(Map.of("message", "Cannot delete admin user"));
            }

            userService.deleteUser(id);
            return ResponseEntity.ok(Map.of("message", "User deleted successfully"));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(Map.of("message", "Error deleting user: " + e.getMessage()));
        }
    }

    @PutMapping("/users/{id}/toggle-status")
    public ResponseEntity<?> toggleUserStatus(@PathVariable Long id) {
        try {
            User user = userService.findUserById(id);

            // Prevent disabling admin
            if (user.getRole() == Role.ADMIN) {
                return ResponseEntity.badRequest()
                        .body(Map.of("message", "Cannot disable admin user"));
            }

            user.setEnabled(!user.isEnabled());
            User updatedUser = userService.updateUser(user);

            // Remove password from response
            updatedUser.setPassword(null);

            return ResponseEntity.ok(updatedUser);
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(Map.of("message", "Error updating user status: " + e.getMessage()));
        }
    }

    @GetMapping("/users/{id}")
    public ResponseEntity<?> getUserById(@PathVariable Long id) {
        try {
            User user = userService.findUserById(id);
            // Remove password from response
            user.setPassword(null);
            return ResponseEntity.ok(user);
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(Map.of("message", "User not found"));
        }
    }
}