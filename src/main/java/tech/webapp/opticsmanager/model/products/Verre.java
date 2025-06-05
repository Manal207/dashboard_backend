package tech.webapp.opticsmanager.model.products;

import jakarta.persistence.*;
import lombok.*;
import tech.webapp.opticsmanager.model.organization.Fournisseur;

import java.math.BigDecimal;

@Entity
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class Verre extends Produit{

    private String nom;
    private String nomInterne;
    private String pht;
    private boolean duret;
    private boolean uv;
    private String ar;
    private String teint;
    private boolean solaire;
    private String remarque;
    private String foyer;
    private String matiere;
    private String disponibilite;
    private String indice;

    private double valeurMin;
    private double valeurMax;

    @Enumerated(EnumType.STRING)
    private Gamme gamme;

    @ManyToOne
    private Fournisseur fournisseur;

    public enum Gamme {
        BASSE, MOYENNE, HAUTE
    }
}
