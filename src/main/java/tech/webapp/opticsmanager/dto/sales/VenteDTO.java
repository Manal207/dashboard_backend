package tech.webapp.opticsmanager.dto.sales;

import lombok.*;
import tech.webapp.opticsmanager.model.sales.Vente;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class VenteDTO {
    private Long id;
    private String numeroVente;
    private Long createurId;
    private String userNom;
    private Vente.TypeClient typeClient;
    private Long clientId;
    private String clientNom; // Nom du client pour affichage
    private LocalDateTime dateCreation;
    private LocalDateTime dateModification;
    private Vente.StatutVente statut;
    private BigDecimal totalHT;
    private BigDecimal totalTTC;
    private String notes;
    private List<FicheVenteDTO> fiches;
}