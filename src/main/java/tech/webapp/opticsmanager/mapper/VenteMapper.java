package tech.webapp.opticsmanager.mapper;

import org.springframework.stereotype.Component;
import tech.webapp.opticsmanager.dto.vente.*;
import tech.webapp.opticsmanager.model.vente.*;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class VenteMapper {

    public VenteDTO toDTO(Vente vente) {
        if (vente == null) return null;

        VenteDTO dto = new VenteDTO();
        dto.setId(vente.getId());
        dto.setNumeroVente(vente.getNumeroVente());
        dto.setClientId(vente.getClientId());
        dto.setClientNom(vente.getClientNom());
        dto.setTypeClient(vente.getTypeClient());
        dto.setDateVente(vente.getDateVente());
        dto.setStatut(vente.getStatut());
        dto.setMontantTotal(vente.getMontantTotal());
        dto.setMontantTVA(vente.getMontantTVA());
        dto.setMontantTTC(vente.getMontantTTC());
        dto.setNotes(vente.getNotes());

        if (vente.getLignesVente() != null) {
            dto.setLignesVente(vente.getLignesVente().stream()
                    .map(this::toLigneVenteDTO)
                    .collect(Collectors.toList()));
        }

        // Add user information
        if (vente.getCreatedBy() != null) {
            dto.setCreatedById(vente.getCreatedBy().getId());
            dto.setCreatedByUsername(vente.getCreatedBy().getUsername());
            dto.setCreatedByName(vente.getCreatedBy().getName());
        }

        return dto;
    }

    public LigneVenteDTO toLigneVenteDTO(LigneVente ligne) {
        if (ligne == null) return null;

        LigneVenteDTO dto = new LigneVenteDTO();
        dto.setId(ligne.getId());
        dto.setTypeProduit(ligne.getTypeProduit());
        dto.setQuantite(ligne.getQuantite());
        dto.setPrixUnitaire(ligne.getPrixUnitaire());
        dto.setRemise(ligne.getRemise());
        dto.setPrixTotal(ligne.getPrixTotal());

        // Set produit info selon le type
        switch (ligne.getTypeProduit()) {
            case MONTURE:
                if (ligne.getMonture() != null) {
                    dto.setProduitId(ligne.getMonture().getId());
                    dto.setProduitNom(ligne.getMonture().getMarque());
                    dto.setProduitReference(ligne.getMonture().getReference());
                }
                break;
            case VERRE:
                if (ligne.getVerre() != null) {
                    dto.setProduitId(ligne.getVerre().getId());
                    dto.setProduitNom(ligne.getVerre().getNom());
                }
                if (ligne.getPrescription() != null) {
                    dto.setPrescription(toPrescriptionDTO(ligne.getPrescription()));
                }
                break;
            // Autres cas...
        }

        return dto;
    }

    public PrescriptionOptiqueDTO toPrescriptionDTO(PrescriptionOptique prescription) {
        if (prescription == null) return null;

        PrescriptionOptiqueDTO dto = new PrescriptionOptiqueDTO();
        // Copier tous les champs
        dto.setOdSphere(prescription.getOdSphere());
        dto.setOdCylindre(prescription.getOdCylindre());
        dto.setOdAxe(prescription.getOdAxe());
        dto.setOdAddition(prescription.getOdAddition());
        dto.setOgSphere(prescription.getOgSphere());
        dto.setOgCylindre(prescription.getOgCylindre());
        dto.setOgAxe(prescription.getOgAxe());
        dto.setOgAddition(prescription.getOgAddition());
        dto.setEcartPupillaire(prescription.getEcartPupillaire());
        dto.setHauteurMontage(prescription.getHauteurMontage());
        dto.setAntiReflet(prescription.isAntiReflet());
        dto.setProtectionUV(prescription.isProtectionUV());
        dto.setPhotochromique(prescription.isPhotochromique());
        dto.setPolarisant(prescription.isPolarisant());

        return dto;
    }

    public PrescriptionOptique toEntity(PrescriptionOptiqueDTO dto) {
        if (dto == null) return null;

        PrescriptionOptique prescription = new PrescriptionOptique();
        prescription.setOdSphere(dto.getOdSphere());
        prescription.setOdCylindre(dto.getOdCylindre());
        prescription.setOdAxe(dto.getOdAxe());
        prescription.setOdAddition(dto.getOdAddition());
        prescription.setOgSphere(dto.getOgSphere());
        prescription.setOgCylindre(dto.getOgCylindre());
        prescription.setOgAxe(dto.getOgAxe());
        prescription.setOgAddition(dto.getOgAddition());
        prescription.setEcartPupillaire(dto.getEcartPupillaire());
        prescription.setHauteurMontage(dto.getHauteurMontage());
        prescription.setAntiReflet(dto.isAntiReflet());
        prescription.setProtectionUV(dto.isProtectionUV());
        prescription.setPhotochromique(dto.isPhotochromique());
        prescription.setPolarisant(dto.isPolarisant());

        return prescription;
    }
}