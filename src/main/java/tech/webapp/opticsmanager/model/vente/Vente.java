package tech.webapp.opticsmanager.model.vente;


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
@Table(name = "ventes")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class Vente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String numeroVente;

    // Client peut être Magasin ou Particulier
    @ManyToOne
    @JoinColumn(name = "magasin_client_id")
    private Magasin magasinClient;

    @ManyToOne
    @JoinColumn(name = "particulier_client_id")
    private Particulier particulierClient;

    @Enumerated(EnumType.STRING)
    @Column(name = "type_client", nullable = false)
    private TypeClient typeClient;

    @OneToMany(mappedBy = "vente", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<LigneVente> lignesVente = new ArrayList<>();

    @Column(nullable = false)
    private LocalDateTime dateVente;

    @Enumerated(EnumType.STRING)
    private StatutVente statut = StatutVente.BROUILLON;

    @Column(precision = 10, scale = 2)
    private BigDecimal montantTotal;

    @Column(precision = 10, scale = 2)
    private BigDecimal montantTVA;

    @Column(precision = 10, scale = 2)
    private BigDecimal montantTTC;

    private String notes;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User createdBy;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    public enum StatutVente {
        BROUILLON, CONFIRMEE, EN_PREPARATION, PRETE, LIVREE, ANNULEE
    }

    public enum TypeClient {
        MAGASIN, PARTICULIER
    }

    // Méthodes utilitaires
    public String getClientNom() {
        if (typeClient == TypeClient.MAGASIN && magasinClient != null) {
            return magasinClient.getNom();
        } else if (typeClient == TypeClient.PARTICULIER && particulierClient != null) {
            return particulierClient.getNom() + " " + particulierClient.getPrenom();
        }
        return "";
    }

    public Long getClientId() {
        if (typeClient == TypeClient.MAGASIN && magasinClient != null) {
            return magasinClient.getId();
        } else if (typeClient == TypeClient.PARTICULIER && particulierClient != null) {
            return particulierClient.getId();
        }
        return null;
    }
}