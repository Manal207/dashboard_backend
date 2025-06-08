package tech.webapp.opticsmanager.model.sales;

import jakarta.persistence.*;
import lombok.*;
import tech.webapp.opticsmanager.model.User;
import tech.webapp.opticsmanager.model.organization.client.Magasin;
import tech.webapp.opticsmanager.model.organization.client.Particulier;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class Vente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String numeroVente; // Auto-généré : V-YYYY-NNNN

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User createur;

    @Column(name = "user_nom")
    private String userNom; // Snapshot du nom au moment de la création

    @Enumerated(EnumType.STRING)
    private TypeClient typeClient;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "magasin_id")
    private Magasin magasin;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "particulier_id")
    private Particulier particulier;

    @Column(name = "date_creation")
    private LocalDateTime dateCreation;

    @Column(name = "date_modification")
    private LocalDateTime dateModification;

    @Enumerated(EnumType.STRING)
    private StatutVente statut;

    @Column(name = "total_ht", precision = 10, scale = 2)
    private BigDecimal totalHT;

    @Column(name = "total_ttc", precision = 10, scale = 2)
    private BigDecimal totalTTC;

    @Column(length = 1000)
    private String notes;

    @OneToMany(mappedBy = "vente", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<FicheVente> fiches = new ArrayList<>(); // ← INITIALISATION AJOUTÉE

//    @OneToMany(mappedBy = "vente", cascade = CascadeType.ALL, orphanRemoval = true)
//    private List<FicheVente> fiches;

    public enum TypeClient {
        MAGASIN, PARTICULIER
    }

    public enum StatutVente {
        BROUILLON, CONFIRMEE, FACTUREE, LIVREE, ANNULEE
    }

    @PrePersist
    protected void onCreate() {
        dateCreation = LocalDateTime.now();
        dateModification = LocalDateTime.now();
        if (statut == null) {
            statut = StatutVente.BROUILLON;
        }
        if (numeroVente == null) {
            // Sera généré par le service
        }
    }

    @PreUpdate
    protected void onUpdate() {
        dateModification = LocalDateTime.now();
    }
}