package tech.webapp.opticsmanager.dto.sales;

import lombok.*;
import java.math.BigDecimal;
import java.util.List;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class FicheVenteDTO {
    private Long id;
    private Integer ordre;

    // Monture
    private Long montureId;
    private String montureNom;
    private BigDecimal prixMonture;

    // Verres
    private Long verreODId;
    private String verreODNom;
    private BigDecimal prixVerreOD;

    private Long verreOGId;
    private String verreOGNom;
    private BigDecimal prixVerreOG;

    // Mesures
    private MesuresOeilDTO mesuresOD;
    private MesuresOeilDTO mesuresOG;

    // Accessoires
    private List<FicheAccessoireDTO> accessoires;

    private BigDecimal totalFiche;
}