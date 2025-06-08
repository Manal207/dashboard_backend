package tech.webapp.opticsmanager.model.sales;

import jakarta.persistence.*;
import lombok.*;
import tech.webapp.opticsmanager.model.products.Accessoire;

import java.math.BigDecimal;

@Entity
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class FicheAccessoire {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fiche_vente_id")
    private FicheVente ficheVente;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "accessoire_id")
    private Accessoire accessoire;

    @Column(name = "prix_accessoire", precision = 10, scale = 2)
    private BigDecimal prixAccessoire;

    @Column(name = "ordre_accessoire")
    private Integer ordre;
}