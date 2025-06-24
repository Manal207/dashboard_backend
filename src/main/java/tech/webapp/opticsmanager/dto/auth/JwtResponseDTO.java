package tech.webapp.opticsmanager.dto.auth;

import lombok.Data;
import tech.webapp.opticsmanager.model.Role;

@Data
public class JwtResponseDTO {
    private String token;
    private String type = "Bearer";
    private Long id;
    private String username;
    private String email;
    private String name;
    private Role role;

    public JwtResponseDTO(String token, Long id, String username, String email, String name, Role role) {
        this.token = token;
        this.id = id;
        this.username = username;
        this.email = email;
        this.name = name;
        this.role = role;
    }
}
//package tech.webapp.opticsmanager.dto.auth;
//
//import lombok.Data;
//
//@Data
//public class JwtResponseDTO {
//    private String token;
//    private String type = "Bearer";
//    private Long id;
//    private String username;
//    private String email;
//    private String name;
//
//    public JwtResponseDTO(String token, Long id, String username, String email, String name) {
//        this.token = token;
//        this.id = id;
//        this.username = username;
//        this.email = email;
//        this.name = name;
//    }
//}
