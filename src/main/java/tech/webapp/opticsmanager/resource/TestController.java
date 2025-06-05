package tech.webapp.opticsmanager.resource;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import tech.webapp.opticsmanager.model.User;

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
}