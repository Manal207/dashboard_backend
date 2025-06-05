package tech.webapp.opticsmanager.dto.vente;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;
import tech.webapp.opticsmanager.model.vente.Vente;

import jakarta.validation.constraints.NotNull;
import java.util.List;

@Data
public class CreateVenteDTO {

    @NotNull(message = "Client ID is required")
    private Long clientId;

    @NotNull(message = "Type client is required")
    private Vente.TypeClient typeClient;

    @NotEmpty(message = "At least one ligne vente is required")
    private List<CreateLigneVenteDTO> lignesVente;

    private String notes;

//    @NotNull
//    private Long clientId;
//
//    @NotNull
//    private Vente.TypeClient typeClient;
//
//    private List<CreateLigneVenteDTO> lignesVente;
//
//    private String notes;
}