package tech.webapp.opticsmanager.model.products;
import jakarta.persistence.*;
import lombok.*;
import tech.webapp.opticsmanager.model.organization.client.Magasin;


@Entity
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class Monture extends Produit {

    private String marque;
    private String reference;
    private String couleur;
    private String branche;

    @Enumerated(EnumType.STRING)
    private Forme forme;

    @Enumerated(EnumType.STRING)
    private Genre genre;

    @Enumerated(EnumType.STRING)
    private Matiere matiere;

    @Enumerated(EnumType.STRING)
    @Column(name = "type_montage")
    private TypeMontage typeMontage;

    @Enumerated(EnumType.STRING)
    private Gamme gamme;

    @Column(name = "quantite_initiale")
    private int quantiteInitiale;

    private String numero;

    @ManyToOne
    private Magasin magasin;

    public enum Forme {
        CARRE, CARPE, CERCLE, OVALE, AVIATEUR, RECT, RONDE, PANTOS, CLUBMASTER, WAYFARER, PAPILLON, MASQUE
    }

    public enum Genre {
        HOMME, FEMME, ENFANT, JUNIOR, MIXTE
    }

    public enum Matiere {
        METAL, PLASTIQUE
    }

    public enum TypeMontage {
        PERCE, NYLOR, CERCLE, SOLAIRE
    }

    public enum Gamme {
        GAMME_1, GAMME_2, GAMME_3
    }
}
