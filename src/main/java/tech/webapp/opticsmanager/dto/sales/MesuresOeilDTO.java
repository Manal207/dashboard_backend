package tech.webapp.opticsmanager.dto.sales;

import lombok.*;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class MesuresOeilDTO {

    @DecimalMin(value = "-30.0", message = "SPH doit être entre -30 et +30")
    @DecimalMax(value = "30.0", message = "SPH doit être entre -30 et +30")
    private Double sph; // Sphère

    @DecimalMin(value = "-10.0", message = "CYL doit être entre -10 et +10")
    @DecimalMax(value = "10.0", message = "CYL doit être entre -10 et +10")
    private Double cyl; // Cylindre

    @DecimalMin(value = "0.0", message = "AXE doit être entre 0 et 180")
    @DecimalMax(value = "180.0", message = "AXE doit être entre 0 et 180")
    private Double axe; // Axe
}