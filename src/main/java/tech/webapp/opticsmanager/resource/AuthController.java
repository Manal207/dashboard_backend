package tech.webapp.opticsmanager.resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import tech.webapp.opticsmanager.dto.auth.JwtResponseDTO;
import tech.webapp.opticsmanager.dto.auth.LoginDTO;
import tech.webapp.opticsmanager.model.User;
import tech.webapp.opticsmanager.security.JwtUtils;
import tech.webapp.opticsmanager.service.UserService;

import jakarta.validation.Valid;

import java.util.Map;


@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    UserService userService;

    @Autowired
    JwtUtils jwtUtils;

    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginDTO loginRequest) {
        try {
            User user = userService.findUserByUsername(loginRequest.getUsername());

            // ✅ CHECK IF USER IS ENABLED
            if (!user.isEnabled()) {
                return ResponseEntity.badRequest()
                        .body(Map.of("message", "Compte désactivé. Contactez l'administrateur."));
            }

            // Check password
            if (!userService.checkPassword(loginRequest.getPassword(), user.getPassword())) {
                return ResponseEntity.badRequest()
                        .body(Map.of("message", "Nom d'utilisateur ou mot de passe invalide"));
            }

            String jwt = jwtUtils.generateJwtToken(user);

            JwtResponseDTO response = new JwtResponseDTO(
                    jwt,
                    user.getId(),
                    user.getUsername(),
                    user.getEmail(),
                    user.getName(),
                    user.getRole()
            );

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(Map.of("message", "Nom d'utilisateur ou mot de passe invalide"));
        }
    }

//    @PostMapping("/signin")
//    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginDTO loginRequest) {
//        try {
//            User user = userService.findUserByUsername(loginRequest.getUsername());
//
//            if (!userService.checkPassword(loginRequest.getPassword(), user.getPassword())) {
//                return ResponseEntity.badRequest()
//                        .body(Map.of("message", "Invalid username or password"));
//            }
//
//            String jwt = jwtUtils.generateJwtToken(user);
//
//            JwtResponseDTO response = new JwtResponseDTO(
//                    jwt,
//                    user.getId(),
//                    user.getUsername(),
//                    user.getEmail(),
//                    user.getName(),
//                    user.getRole()
//            );
//
//            return ResponseEntity.ok(response);
//        } catch (Exception e) {
//            return ResponseEntity.badRequest()
//                    .body(Map.of("message", "Invalid username or password"));
//        }
//    }

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@Valid @RequestBody User signUpRequest) {
        if (userService.existsByUsername(signUpRequest.getUsername())) {
            return ResponseEntity.badRequest().body("Username is already taken!");
        }

        if (userService.existsByEmail(signUpRequest.getEmail())) {
            return ResponseEntity.badRequest().body("Email is already in use!");
        }

        User user = userService.createUser(signUpRequest);

        return ResponseEntity.ok("User registered successfully!");
    }
}