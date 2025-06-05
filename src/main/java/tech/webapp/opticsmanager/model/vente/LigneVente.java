package tech.webapp.opticsmanager.model.vente;


import jakarta.persistence.*;
import lombok.*;
import tech.webapp.opticsmanager.model.products.*;

import java.math.BigDecimal;

@Entity
@Table(name = "lignes_vente")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class LigneVente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "vente_id")
    private Vente vente;

    @Enumerated(EnumType.STRING)
    @Column(name = "type_produit")
    private TypeProduit typeProduit;

    @ManyToOne
    @JoinColumn(name = "monture_id")
    private Monture monture;

    @ManyToOne
    @JoinColumn(name = "verre_id")
    private Verre verre;

    @ManyToOne
    @JoinColumn(name = "lentille_id")
    private Lentille lentille;

    @ManyToOne
    @JoinColumn(name = "accessoire_id")
    private Accessoire accessoire;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "prescription_id")
    private PrescriptionOptique prescription;

    @Column(nullable = false)
    private Integer quantite;

    @Column(name = "prix_unitaire", precision = 10, scale = 2, nullable = false)
    private BigDecimal prixUnitaire;

    @Column(precision = 10, scale = 2)
    private BigDecimal remise;

    @Column(name = "prix_total", precision = 10, scale = 2)
    private BigDecimal prixTotal;

    public enum TypeProduit {
        MONTURE, VERRE, LENTILLE, ACCESSOIRE
    }
}