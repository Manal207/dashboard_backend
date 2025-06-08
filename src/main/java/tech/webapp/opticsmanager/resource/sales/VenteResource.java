package tech.webapp.opticsmanager.resource.sales;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import tech.webapp.opticsmanager.dto.sales.*;
import tech.webapp.opticsmanager.model.User;
import tech.webapp.opticsmanager.service.sales.VenteService;

import jakarta.validation.Valid;
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
}