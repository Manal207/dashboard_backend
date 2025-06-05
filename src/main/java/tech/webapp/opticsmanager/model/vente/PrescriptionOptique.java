package tech.webapp.opticsmanager.model.vente;


import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(name = "prescriptions_optiques")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class PrescriptionOptique {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Œil Droit (OD)
    @Column(name = "od_sphere", precision = 5, scale = 2)
    private BigDecimal odSphere;

    @Column(name = "od_cylindre", precision = 5, scale = 2)
    private BigDecimal odCylindre;

    @Column(name = "od_axe")
    private Integer odAxe;

    @Column(name = "od_addition", precision = 5, scale = 2)
    private BigDecimal odAddition;

    // Œil Gauche (OG)
    @Column(name = "og_sphere", precision = 5, scale = 2)
    private BigDecimal ogSphere;

    @Column(name = "og_cylindre", precision = 5, scale = 2)
    private BigDecimal ogCylindre;

    @Column(name = "og_axe")
    private Integer ogAxe;

    @Column(name = "og_addition", precision = 5, scale = 2)
    private BigDecimal ogAddition;

    // Mesures supplémentaires
    @Column(name = "ecart_pupillaire", precision = 5, scale = 2)
    private BigDecimal ecartPupillaire;

    @Column(name = "hauteur_montage", precision = 5, scale = 2)
    private BigDecimal hauteurMontage;

    // Options de traitement
    @Column(name = "anti_reflet")
    private boolean antiReflet;

    @Column(name = "protection_uv")
    private boolean protectionUV;

    private boolean photochromique;

    private boolean polarisant;
}
