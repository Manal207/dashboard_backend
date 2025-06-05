package tech.webapp.opticsmanager.dto.vente;

import lombok.Data;
import tech.webapp.opticsmanager.model.vente.Vente;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class VenteDTO {
    private Long id;
    private String numeroVente;
    private Long clientId;
    private String clientNom;
    private Vente.TypeClient typeClient;
    private List<LigneVenteDTO> lignesVente;
    private LocalDateTime dateVente;
    private Vente.StatutVente statut;
    private BigDecimal montantTotal;
    private BigDecimal montantTVA;
    private BigDecimal montantTTC;
    private String notes;

    private Long createdById;
    private String createdByUsername;
    private String createdByName;

}