package tech.webapp.opticsmanager.dto.sales;

import lombok.*;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class UpdateAccessoireRequest {

    private Long id; // Si null = nouvel accessoire, sinon modification

    @NotNull
    private Long accessoireId;

    private BigDecimal prixAccessoire;
    private Integer ordre;
}