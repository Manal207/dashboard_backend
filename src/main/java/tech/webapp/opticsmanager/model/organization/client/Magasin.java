package tech.webapp.opticsmanager.model.organization.client;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;

@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})

@Entity
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class Magasin {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nom;
    private String code;
    private String adresse;
    private String ville;
    private String tel;
    private String type;
    private String statut;
    private String email;
    private String ice;
    private String societe;
    private String username;
    private String motDePasse;
    private double remise;

}
