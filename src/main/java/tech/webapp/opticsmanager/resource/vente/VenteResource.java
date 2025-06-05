package tech.webapp.opticsmanager.resource.vente;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.webapp.opticsmanager.dto.vente.CreateLigneVenteDTO;
import tech.webapp.opticsmanager.dto.vente.CreateVenteDTO;
import tech.webapp.opticsmanager.dto.vente.VenteDTO;
import tech.webapp.opticsmanager.model.vente.Vente;
import tech.webapp.opticsmanager.model.vente.LigneVente;
import tech.webapp.opticsmanager.service.vente.VenteService;

import java.util.List;

@RestController
@RequestMapping("/vente")
public class VenteResource {

    private final VenteService venteService;

    public VenteResource(VenteService venteService) {
        this.venteService = venteService;
    }

    @GetMapping("/all")
    public ResponseEntity<List<VenteDTO>> getAllVentes() {
        List<VenteDTO> ventes = venteService.findAllVentesDTO();
        return new ResponseEntity<>(ventes, HttpStatus.OK);
    }

    @GetMapping("/find/{id}")
    public ResponseEntity<VenteDTO> getVenteById(@PathVariable("id") Long id) {
        VenteDTO vente = venteService.findVenteByIdDTO(id);
        return new ResponseEntity<>(vente, HttpStatus.OK);
    }

    @PostMapping("/add")
    public ResponseEntity<VenteDTO> createVente(@Valid @RequestBody CreateVenteDTO createDTO) {
        System.out.println("Received vente data: " + createDTO);
        System.out.println("Client ID: " + createDTO.getClientId());
        System.out.println("Type Client: " + createDTO.getTypeClient());
        System.out.println("Lignes Vente size: " + (createDTO.getLignesVente() != null ? createDTO.getLignesVente().size() : 0));

        try {
            VenteDTO vente = venteService.createVenteFromDTO(createDTO);
            return new ResponseEntity<>(vente, HttpStatus.CREATED);
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

//    @PostMapping("/add")
//    public ResponseEntity<VenteDTO> createVente(@Valid @RequestBody CreateVenteDTO createDTO) {
//        VenteDTO vente = venteService.createVenteFromDTO(createDTO);
//        return new ResponseEntity<>(vente, HttpStatus.CREATED);
//    }

    @PostMapping("/{id}/ligne")
    public ResponseEntity<VenteDTO> addLigneVente(@PathVariable("id") Long venteId,
                                                  @Valid @RequestBody CreateLigneVenteDTO ligneDTO) {
        VenteDTO vente = venteService.addLigneVenteFromDTO(venteId, ligneDTO);
        return new ResponseEntity<>(vente, HttpStatus.OK);
    }



// vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv

//    private final VenteService venteService;
//
//    public VenteResource(VenteService venteService) {
//        this.venteService = venteService;
//    }
//
//    @GetMapping("/all")
//    public ResponseEntity<List<Vente>> getAllVentes() {
//        List<Vente> ventes = venteService.findAllVentes();
//        return new ResponseEntity<>(ventes, HttpStatus.OK);
//    }
//
//    @GetMapping("/find/{id}")
//    public ResponseEntity<Vente> getVenteById(@PathVariable("id") Long id) {
//        Vente vente = venteService.findVenteById(id);
//        return new ResponseEntity<>(vente, HttpStatus.OK);
//    }

    @GetMapping("/find/{id}/details")
    public ResponseEntity<Vente> getVenteWithDetails(@PathVariable("id") Long id) {
        Vente vente = venteService.findVenteWithLignes(id);
        return new ResponseEntity<>(vente, HttpStatus.OK);
    }

//    @PostMapping("/add")
//    public ResponseEntity<Vente> addVente(@RequestBody Vente vente) {
//        Vente newVente = venteService.addVente(vente);
//        return new ResponseEntity<>(newVente, HttpStatus.CREATED);
//    }

    @PutMapping("/update")
    public ResponseEntity<Vente> updateVente(@RequestBody Vente vente) {
        Vente updatedVente = venteService.updateVente(vente);
        return new ResponseEntity<>(updatedVente, HttpStatus.OK);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteVente(@PathVariable("id") Long id) {
        venteService.deleteVente(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping("/{id}/statut")
    public ResponseEntity<Vente> updateStatut(@PathVariable("id") Long id,
                                              @RequestParam("statut") Vente.StatutVente statut) {
        Vente vente = venteService.updateStatut(id, statut);
        return new ResponseEntity<>(vente, HttpStatus.OK);
    }

//    @PostMapping("/{id}/ligne")
//    public ResponseEntity<Vente> addLigneVente(@PathVariable("id") Long venteId,
//                                               @RequestBody LigneVente ligneVente) {
//        Vente vente = venteService.addLigneVente(venteId, ligneVente);
//        return new ResponseEntity<>(vente, HttpStatus.OK);
//    }

    @DeleteMapping("/{venteId}/ligne/{ligneId}")
    public ResponseEntity<Vente> removeLigneVente(@PathVariable("venteId") Long venteId,
                                                  @PathVariable("ligneId") Long ligneId) {
        Vente vente = venteService.removeLigneVente(venteId, ligneId);
        return new ResponseEntity<>(vente, HttpStatus.OK);
    }

    @PutMapping("/{id}/client/magasin/{magasinId}")
    public ResponseEntity<Vente> setClientMagasin(@PathVariable("id") Long venteId,
                                                  @PathVariable("magasinId") Long magasinId) {
        Vente vente = venteService.setClientMagasin(venteId, magasinId);
        return new ResponseEntity<>(vente, HttpStatus.OK);
    }

    @PutMapping("/{id}/client/particulier/{particulierId}")
    public ResponseEntity<Vente> setClientParticulier(@PathVariable("id") Long venteId,
                                                      @PathVariable("particulierId") Long particulierId) {
        Vente vente = venteService.setClientParticulier(venteId, particulierId);
        return new ResponseEntity<>(vente, HttpStatus.OK);
    }
}