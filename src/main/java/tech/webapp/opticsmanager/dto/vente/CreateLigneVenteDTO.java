package tech.webapp.opticsmanager.dto.vente;

import lombok.Data;
import tech.webapp.opticsmanager.model.vente.LigneVente;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;


import java.math.BigDecimal;

@Data
public class CreateLigneVenteDTO {

    @NotNull(message = "Type produit is required")
    private LigneVente.TypeProduit typeProduit;

    @NotNull(message = "Produit ID is required")
    private Long produitId;

    private PrescriptionOptiqueDTO prescription;

    @NotNull(message = "Quantite is required")
    @Min(value = 1, message = "Quantite must be at least 1")
    private Integer quantite;

    private BigDecimal remise;

//    private BigDecimal prixUnitaireSaisi;

//    @NotNull
//    private LigneVente.TypeProduit typeProduit;
//
//    @NotNull
//    private Long produitId;
//
//    private PrescriptionOptiqueDTO prescription; // Requis pour les verres
//
//    @NotNull
//    @Min(1)
//    private Integer quantite;
//
//    private BigDecimal remise;
}