package tech.webapp.opticsmanager.resource.sales;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import tech.webapp.opticsmanager.dto.sales.*;
import tech.webapp.opticsmanager.exception.ResourceNotFoundException;
import tech.webapp.opticsmanager.model.User;
import tech.webapp.opticsmanager.model.sales.FicheVente;
import tech.webapp.opticsmanager.model.sales.Vente;
import tech.webapp.opticsmanager.repo.sales.FicheVenteRepository;
import tech.webapp.opticsmanager.repo.sales.VenteRepository;
import tech.webapp.opticsmanager.service.sales.VenteService;

import jakarta.validation.Valid;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/ventes")
@CrossOrigin(origins = "*")
public class VenteResource {

    @Autowired
    private VenteService venteService;

    @PostMapping
    public ResponseEntity<VenteDTO> createVente(
            @Valid @RequestBody CreateVenteRequest request,
            Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        VenteDTO vente = venteService.createVente(request, user.getId());
        return new ResponseEntity<>(vente, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<VenteDTO>> getAllVentes(
            @RequestParam(required = false, defaultValue = "false") Boolean mesVentes,
            Authentication authentication) {

        List<VenteDTO> ventes;

        if (Boolean.TRUE.equals(mesVentes)) {
            User user = (User) authentication.getPrincipal();
            ventes = venteService.findVentesByCreateur(user.getId());
        } else {
            ventes = venteService.findAllVentes();
        }

        return ResponseEntity.ok(ventes);
    }

    @GetMapping("/{id}")
    public ResponseEntity<VenteDTO> getVenteById(@PathVariable Long id) {
        VenteDTO vente = venteService.findVenteById(id);
        return ResponseEntity.ok(vente);
    }

    @PutMapping("/{id}")
    public ResponseEntity<VenteDTO> updateVente(
            @PathVariable Long id,
            @Valid @RequestBody UpdateVenteRequest request) {
        VenteDTO vente = venteService.updateVente(id, request);
        return ResponseEntity.ok(vente);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteVente(@PathVariable Long id) {
        venteService.deleteVente(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/fiches")
    public ResponseEntity<FicheVenteDTO> addFiche(
            @PathVariable Long id,
            @Valid @RequestBody CreateFicheRequest request) {
        FicheVenteDTO fiche = venteService.addFiche(id, request);
        return new ResponseEntity<>(fiche, HttpStatus.CREATED);
    }

    @GetMapping("/search")
    public ResponseEntity<List<VenteDTO>> searchVentes(
            @RequestParam(required = false) String numeroVente,
            @RequestParam(required = false) Long createurId,
            @RequestParam(required = false) String typeClient,
            @RequestParam(required = false) String statut) {

        // Implémentation de la recherche via le service
        // TODO: Créer méthode searchVentes dans VenteService

        return ResponseEntity.ok(venteService.findAllVentes());
    }


// Add this endpoint to VenteResource.java for manual testing:

    VenteRepository venteRepository;
    FicheVenteRepository ficheVenteRepository;

    @PostMapping("/{id}/fix-totals")

    public ResponseEntity<VenteDTO> fixVenteTotals(@PathVariable Long id) {
        // Get the vente
        Vente vente = venteRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Vente not found with id: " + id));

        // Get all fiches and calculate total manually
        List<FicheVente> fiches = ficheVenteRepository.findByVenteOrderByOrdre(vente);

        BigDecimal totalHT = fiches.stream()
                .map(fiche -> fiche.getTotalFiche() != null ? fiche.getTotalFiche() : BigDecimal.ZERO)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        // Update vente totals
        vente.setTotalHT(totalHT);
        vente.setTotalTTC(totalHT);

        // Save
        venteRepository.save(vente);

        // Return updated vente
        return ResponseEntity.ok(venteService.findVenteById(id));
    }
}