package tech.webapp.opticsmanager.dto.sales;

import lombok.*;
import tech.webapp.opticsmanager.model.sales.Vente;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.util.List;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class CreateVenteRequest {

    @NotNull
    private Vente.TypeClient typeClient;

    @NotNull
    private Long clientId;

    private String notes;

    @Valid
    private List<CreateFicheRequest> fiches;
}