package tech.webapp.opticsmanager.service.vente;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.webapp.opticsmanager.model.vente.LigneVente;
import tech.webapp.opticsmanager.model.vente.PrescriptionOptique;
import tech.webapp.opticsmanager.repo.vente.LigneVenteRepo;

import java.util.List;

@Service
@Transactional
public class LigneVenteService {

    private final LigneVenteRepo ligneVenteRepo;

    @Autowired
    public LigneVenteService(LigneVenteRepo ligneVenteRepo) {
        this.ligneVenteRepo = ligneVenteRepo;
    }

    public LigneVente addLigneVente(LigneVente ligneVente) {
        return ligneVenteRepo.save(ligneVente);
    }

    public LigneVente updateLigneVente(LigneVente ligneVente) {
        return ligneVenteRepo.save(ligneVente);
    }

    public void deleteLigneVente(Long id) {
        ligneVenteRepo.deleteLigneVenteById(id);
    }

    public LigneVente findLigneVenteById(Long id) {
        return ligneVenteRepo.findLigneVenteById(id)
                .orElseThrow(() -> new RuntimeException("ligneVente not found"));
    }

    public List<LigneVente> findLignesVenteByVenteId(Long venteId) {
        return ligneVenteRepo.findByVenteId(venteId);
    }

    public LigneVente updatePrescription(Long ligneVenteId, PrescriptionOptique prescription) {
        LigneVente ligneVente = findLigneVenteById(ligneVenteId);

        if (ligneVente.getTypeProduit() != LigneVente.TypeProduit.VERRE) {
            throw new IllegalArgumentException("Prescription can only be added to VERRE type");
        }

        ligneVente.setPrescription(prescription);
        return ligneVenteRepo.save(ligneVente);
    }
}