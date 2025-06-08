package tech.webapp.opticsmanager.model.sales;

import jakarta.persistence.*;
import lombok.*;
import tech.webapp.opticsmanager.model.products.Monture;
import tech.webapp.opticsmanager.model.products.Verre;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class FicheVente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "vente_id")
    private Vente vente;

    @Column(name = "ordre_fiche")
    private Integer ordre;

    // Monture
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "monture_id")
    private Monture monture;

    @Column(name = "prix_monture", precision = 10, scale = 2)
    private BigDecimal prixMonture;

    // Verre Œil Droit
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "verre_od_id")
    private Verre verreOD;

    @Column(name = "prix_verre_od", precision = 10, scale = 2)
    private BigDecimal prixVerreOD;

    // Verre Œil Gauche
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "verre_og_id")
    private Verre verreOG;

    @Column(name = "prix_verre_og", precision = 10, scale = 2)
    private BigDecimal prixVerreOG;

    // Mesures Œil Droit
    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "sph", column = @Column(name = "sph_od")),
            @AttributeOverride(name = "cyl", column = @Column(name = "cyl_od")),
            @AttributeOverride(name = "axe", column = @Column(name = "axe_od"))
    })
    private MesuresOeil mesuresOD;

    // Mesures Œil Gauche
    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "sph", column = @Column(name = "sph_og")),
            @AttributeOverride(name = "cyl", column = @Column(name = "cyl_og")),
            @AttributeOverride(name = "axe", column = @Column(name = "axe_og"))
    })
    private MesuresOeil mesuresOG;

    @OneToMany(mappedBy = "ficheVente", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<FicheAccessoire> accessoires = new ArrayList<>(); // ← INITIALISATION AJOUTÉE

    @Column(name = "total_fiche", precision = 10, scale = 2)
    private BigDecimal totalFiche;

    @PrePersist
    @PreUpdate
    protected void initializeCollections() {
        if (accessoires == null) {
            accessoires = new ArrayList<>(); // ← SÉCURITÉ SUPPLÉMENTAIRE
        }
    }
}





//package tech.webapp.opticsmanager.model.sales;
//
//import jakarta.persistence.*;
//import lombok.*;
//import tech.webapp.opticsmanager.model.products.Monture;
//import tech.webapp.opticsmanager.model.products.Verre;
//
//import java.math.BigDecimal;
//import java.util.List;
//
//@Entity
//@Getter @Setter @NoArgsConstructor @AllArgsConstructor
//public class FicheVente {
//
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long id;
//
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "vente_id")
//    private Vente vente;
//
//    @Column(name = "ordre_fiche")
//    private Integer ordre;
//
//    // Monture
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "monture_id")
//    private Monture monture;
//
//    @Column(name = "prix_monture", precision = 10, scale = 2)
//    private BigDecimal prixMonture;
//
//    // Verre Œil Droit
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "verre_od_id")
//    private Verre verreOD;
//
//    @Column(name = "prix_verre_od", precision = 10, scale = 2)
//    private BigDecimal prixVerreOD;
//
//    // Verre Œil Gauche
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "verre_og_id")
//    private Verre verreOG;
//
//    @Column(name = "prix_verre_og", precision = 10, scale = 2)
//    private BigDecimal prixVerreOG;
//
//    // Mesures Œil Droit
//    @Embedded
//    @AttributeOverrides({
//            @AttributeOverride(name = "sph", column = @Column(name = "sph_od")),
//            @AttributeOverride(name = "cyl", column = @Column(name = "cyl_od")),
//            @AttributeOverride(name = "axe", column = @Column(name = "axe_od"))
//    })
//    private MesuresOeil mesuresOD;
//
//    // Mesures Œil Gauche
//    @Embedded
//    @AttributeOverrides({
//            @AttributeOverride(name = "sph", column = @Column(name = "sph_og")),
//            @AttributeOverride(name = "cyl", column = @Column(name = "cyl_og")),
//            @AttributeOverride(name = "axe", column = @Column(name = "axe_og"))
//    })
//    private MesuresOeil mesuresOG;
//
//    @OneToMany(mappedBy = "ficheVente", cascade = CascadeType.ALL, orphanRemoval = true)
//    private List<FicheAccessoire> accessoires;
//
//    @Column(name = "total_fiche", precision = 10, scale = 2)
//    private BigDecimal totalFiche;
//}