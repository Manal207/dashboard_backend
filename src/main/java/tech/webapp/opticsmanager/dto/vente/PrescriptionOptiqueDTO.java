package tech.webapp.opticsmanager.dto.vente;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class PrescriptionOptiqueDTO {
    // Œil Droit (OD)
    private BigDecimal odSphere;
    private BigDecimal odCylindre;
    private Integer odAxe;
    private BigDecimal odAddition;

    // Œil Gauche (OG)
    private BigDecimal ogSphere;
    private BigDecimal ogCylindre;
    private Integer ogAxe;
    private BigDecimal ogAddition;

    // Mesures supplémentaires
    private BigDecimal ecartPupillaire;
    private BigDecimal hauteurMontage;

    // Options de traitement
    private boolean antiReflet;
    private boolean protectionUV;
    private boolean photochromique;
    private boolean polarisant;
}