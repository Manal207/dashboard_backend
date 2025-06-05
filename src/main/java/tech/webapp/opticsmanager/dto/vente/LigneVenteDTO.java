package tech.webapp.opticsmanager.dto.vente;

import lombok.Data;
import tech.webapp.opticsmanager.model.vente.LigneVente;

import java.math.BigDecimal;

@Data
public class LigneVenteDTO {
    private Long id;
    private LigneVente.TypeProduit typeProduit;
    private Long produitId;
    private String produitNom;
    private String produitReference;
    private PrescriptionOptiqueDTO prescription;
    private Integer quantite;
    private BigDecimal prixUnitaire;
    private BigDecimal remise;
    private BigDecimal prixTotal;
}