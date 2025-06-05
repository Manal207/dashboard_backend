package tech.webapp.opticsmanager.validator;

import org.springframework.stereotype.Component;
import tech.webapp.opticsmanager.dto.vente.PrescriptionOptiqueDTO;
//import tech.webapp.opticsmanager.exception.ValidationException;

import java.math.BigDecimal;

@Component
public class PrescriptionValidator {

    public void validate(PrescriptionOptiqueDTO prescription) {
        if (prescription == null) {
            throw new RuntimeException("Prescription ne peut pas être null pour les verres");
        }

        // Validation de l'axe (doit être entre 0 et 180)
        if (prescription.getOdAxe() != null &&
                (prescription.getOdAxe() < 0 || prescription.getOdAxe() > 180)) {
            throw new RuntimeException("L'axe OD doit être entre 0 et 180 degrés");
        }

        if (prescription.getOgAxe() != null &&
                (prescription.getOgAxe() < 0 || prescription.getOgAxe() > 180)) {
            throw new RuntimeException("L'axe OG doit être entre 0 et 180 degrés");
        }

        // Validation de la sphère (généralement entre -20.00 et +20.00)
        validateSphere(prescription.getOdSphere(), "OD");
        validateSphere(prescription.getOgSphere(), "OG");

        // Validation du cylindre (généralement entre -6.00 et +6.00)
        validateCylindre(prescription.getOdCylindre(), "OD");
        validateCylindre(prescription.getOgCylindre(), "OG");

        // Validation de l'addition (généralement entre +0.75 et +3.50)
        validateAddition(prescription.getOdAddition(), "OD");
        validateAddition(prescription.getOgAddition(), "OG");
    }

    private void validateSphere(BigDecimal sphere, String oeil) {
        if (sphere != null) {
            if (sphere.compareTo(new BigDecimal("-20.00")) < 0 ||
                    sphere.compareTo(new BigDecimal("20.00")) > 0) {
                throw new RuntimeException(
                        String.format("La sphère %s doit être entre -20.00 et +20.00", oeil)
                );
            }
        }
    }

    private void validateCylindre(BigDecimal cylindre, String oeil) {
        if (cylindre != null) {
            if (cylindre.compareTo(new BigDecimal("-6.00")) < 0 ||
                    cylindre.compareTo(new BigDecimal("6.00")) > 0) {
                throw new RuntimeException(
                        String.format("Le cylindre %s doit être entre -6.00 et +6.00", oeil)
                );
            }
        }
    }

    private void validateAddition(BigDecimal addition, String oeil) {
        if (addition != null) {
            if (addition.compareTo(new BigDecimal("0.75")) < 0 ||
                    addition.compareTo(new BigDecimal("3.50")) > 0) {
                throw new RuntimeException(
                        String.format("L'addition %s doit être entre +0.75 et +3.50", oeil)
                );
            }
        }
    }
}