package tech.webapp.opticsmanager;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import tech.webapp.opticsmanager.model.User;
import tech.webapp.opticsmanager.service.UserService;

@Configuration
public class DataInitializer {

    @Bean
    CommandLineRunner init(UserService userService) {
        return args -> {
            // Create admin user if not exists
            if (!userService.existsByUsername("admin")) {
                User admin = new User();
                admin.setUsername("admin");
                admin.setPassword("admin123"); // Will be encoded
                admin.setEmail("admin@opticsmanager.com");
                admin.setName("Administrator");
                userService.createUser(admin);
                System.out.println("Admin user created with username: admin and password: admin123");
            }
        };
    }
}