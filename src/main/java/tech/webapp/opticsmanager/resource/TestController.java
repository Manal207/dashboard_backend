package tech.webapp.opticsmanager.resource;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import tech.webapp.opticsmanager.model.User;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/test")
public class TestController {

    @GetMapping("/public")
    public ResponseEntity<?> publicAccess() {
        return ResponseEntity.ok("Public content");
    }

    @GetMapping("/user")
    public ResponseEntity<?> userAccess(Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        return ResponseEntity.ok("Hello " + user.getName());
    }

    @GetMapping("/whoami")
    public ResponseEntity<?> whoAmI(Authentication authentication) {
        if (authentication == null) {
            return ResponseEntity.ok(Map.of("message", "Not authenticated"));
        }

        User user = (User) authentication.getPrincipal();
        Map<String, Object> response = new HashMap<>();
        response.put("id", user.getId());
        response.put("username", user.getUsername());
        response.put("name", user.getName());
        response.put("email", user.getEmail());
        response.put("role", user.getRole());
        response.put("enabled", user.isEnabled());
        return ResponseEntity.ok(response);
    }
}