package tech.webapp.opticsmanager.model.organization.client;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Particulier {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nom;
    private String prenom;
    private String code;
    private String adresse;
    private String ville;
    private String tel;
    private String type;
    private String statut;
    private String email;
    private String username;
    private String motDePasse;
    private double remise;
}