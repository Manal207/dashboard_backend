package tech.webapp.opticsmanager.dto.sales;

import lombok.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;
import java.util.List;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class UpdateFicheRequest {

    @NotNull
    private Long id; // ID de la fiche à modifier

    private Integer ordre;

    // Monture
    private Long montureId;
    private BigDecimal prixMonture;

    // Verres
    private Long verreODId;
    private BigDecimal prixVerreOD;

    private Long verreOGId;
    private BigDecimal prixVerreOG;

    // Mesures
    @Valid
    private MesuresOeilDTO mesuresOD;

    @Valid
    private MesuresOeilDTO mesuresOG;

    // Accessoires
    @Size(max = 3, message = "Maximum 3 accessoires autorisés par fiche")
    @Valid
    private List<UpdateAccessoireRequest> accessoires;

    private List<Long> accessoiresASupprimer; // IDs des accessoires à supprimer
}