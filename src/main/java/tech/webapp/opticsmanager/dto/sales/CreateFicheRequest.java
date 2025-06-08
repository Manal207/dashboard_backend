package tech.webapp.opticsmanager.dto.sales;

import lombok.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;
import java.util.List;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class CreateFicheRequest {

    @NotNull
    private Integer ordre;

    // Monture
    @NotNull
    private Long montureId;

    private BigDecimal prixMonture; // Si null, utiliser le prix par défaut

    // Verre Œil Droit
    @NotNull
    private Long verreODId;

    private BigDecimal prixVerreOD;

    // Verre Œil Gauche
    @NotNull
    private Long verreOGId;

    private BigDecimal prixVerreOG;

    // Mesures
    @Valid
    @NotNull
    private MesuresOeilDTO mesuresOD;

    @Valid
    @NotNull
    private MesuresOeilDTO mesuresOG;

    // Accessoires (max 3)
    @Size(max = 3, message = "Maximum 3 accessoires autorisés par fiche")
    @Valid
    private List<CreateAccessoireRequest> accessoires;
}