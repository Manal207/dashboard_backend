package tech.webapp.opticsmanager.resource.vente;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.webapp.opticsmanager.model.vente.LigneVente;
import tech.webapp.opticsmanager.model.vente.PrescriptionOptique;
import tech.webapp.opticsmanager.service.vente.LigneVenteService;

import java.util.List;

@RestController
@RequestMapping("/ligne-vente")
public class LigneVenteResource {

    private final LigneVenteService ligneVenteService;

    public LigneVenteResource(LigneVenteService ligneVenteService) {
        this.ligneVenteService = ligneVenteService;
    }

    @GetMapping("/find/{id}")
    public ResponseEntity<LigneVente> getLigneVenteById(@PathVariable("id") Long id) {
        LigneVente ligneVente = ligneVenteService.findLigneVenteById(id);
        return new ResponseEntity<>(ligneVente, HttpStatus.OK);
    }

    @GetMapping("/vente/{venteId}")
    public ResponseEntity<List<LigneVente>> getLignesVenteByVenteId(@PathVariable("venteId") Long venteId) {
        List<LigneVente> lignesVente = ligneVenteService.findLignesVenteByVenteId(venteId);
        return new ResponseEntity<>(lignesVente, HttpStatus.OK);
    }

    @PutMapping("/update")
    public ResponseEntity<LigneVente> updateLigneVente(@RequestBody LigneVente ligneVente) {
        LigneVente updatedLigneVente = ligneVenteService.updateLigneVente(ligneVente);
        return new ResponseEntity<>(updatedLigneVente, HttpStatus.OK);
    }

    @PutMapping("/{id}/prescription")
    public ResponseEntity<LigneVente> updatePrescription(@PathVariable("id") Long ligneVenteId,
                                                         @RequestBody PrescriptionOptique prescription) {
        LigneVente ligneVente = ligneVenteService.updatePrescription(ligneVenteId, prescription);
        return new ResponseEntity<>(ligneVente, HttpStatus.OK);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteLigneVente(@PathVariable("id") Long id) {
        ligneVenteService.deleteLigneVente(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}