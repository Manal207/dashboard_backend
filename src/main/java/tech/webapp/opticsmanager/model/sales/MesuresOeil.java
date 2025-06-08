package tech.webapp.opticsmanager.model.sales;

import jakarta.persistence.Embeddable;
import lombok.*;

@Embeddable
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class MesuresOeil {
    private Double sph; // Sphère
    private Double cyl; // Cylindre
    private Double axe; // Axe
}