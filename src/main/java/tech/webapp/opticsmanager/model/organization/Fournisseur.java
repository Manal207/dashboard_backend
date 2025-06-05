package tech.webapp.opticsmanager.model.organization;
import jakarta.persistence.*;
import lombok.*;
import tech.webapp.opticsmanager.model.organization.client.Magasin;

import java.util.List;

@Entity
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class Fournisseur {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nom;
    private String adresse;

    @ElementCollection
    private List<String> villes;

    private String tel;

    @ElementCollection
    private List<String> typesProduits; // Ex: Verres, Lentilles, Montures

    @ElementCollection
    private List<String> emails;

    @ElementCollection
    private List<String> responsables;

    @ElementCollection
    private List<String> telsResponsables;

    private String gerant;
    private String telGerant;

    private double remiseCaisse;

    private String description;

    @ManyToMany
    private List <Magasin> magasins;
}
