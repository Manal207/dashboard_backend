package tech.webapp.opticsmanager.dto.sales;

import lombok.*;
import java.math.BigDecimal;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class FicheAccessoireDTO {
    private Long id;
    private Long accessoireId;
    private String accessoireNom;
    private String accessoireReference;
    private BigDecimal prixAccessoire;
    private Integer ordre;
}