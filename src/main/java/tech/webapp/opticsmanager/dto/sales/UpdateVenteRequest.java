package tech.webapp.opticsmanager.dto.sales;

import lombok.*;
import tech.webapp.opticsmanager.model.sales.Vente;
import jakarta.validation.Valid;
import java.util.List;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class UpdateVenteRequest {

    private Vente.TypeClient typeClient;
    private Long clientId;
    private String notes;
    private Vente.StatutVente statut;

    @Valid
    private List<UpdateFicheRequest> fiches; // Pour modifier les fiches existantes

    @Valid
    private List<CreateFicheRequest> nouvelleFiches; // Pour ajouter de nouvelles fiches

    private List<Long> fichesASupprimer; // IDs des fiches à supprimer
}