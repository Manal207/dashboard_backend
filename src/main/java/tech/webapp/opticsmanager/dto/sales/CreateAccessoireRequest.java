package tech.webapp.opticsmanager.dto.sales;

import lombok.*;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class CreateAccessoireRequest {

    @NotNull
    private Long accessoireId;

    private BigDecimal prixAccessoire; // Si null, utiliser le prix par défaut

    private Integer ordre; // Ordre dans la liste des accessoires de la fiche
}