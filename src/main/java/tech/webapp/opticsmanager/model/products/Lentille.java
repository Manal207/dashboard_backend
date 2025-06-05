package tech.webapp.opticsmanager.model.products;

import jakarta.persistence.*;
import lombok.*;
import tech.webapp.opticsmanager.model.organization.Fournisseur;

@Entity
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class Lentille extends Produit{

    private String designation;
    private String marque;
    private String modele;
    private String matiere;
    private String duree;
    private String h2o;
    private String o2;
    private String teint;
    private String couleur;
    private boolean uv;
    private boolean masque;

    private double sphMin;
    private double sphMax;
    private String cylMin;
    private String cylMax;

    private String rayonInf;
    private String axe;
    private String diametre;
    private String foyer;
    private String disponibilite;

    private boolean ajouterAuCatalogue;
    private double pv;
    private double remisePrn;
    private double promoClient;
    private double promoFournisseur;

    @ManyToOne
    private Fournisseur fournisseur;
}
