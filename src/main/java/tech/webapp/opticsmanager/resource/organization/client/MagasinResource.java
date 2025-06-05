package tech.webapp.opticsmanager.resource.organization.client;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.webapp.opticsmanager.model.organization.client.Magasin;
import tech.webapp.opticsmanager.service.organization.client.MagasinService;

import java.util.List;

@RestController
@RequestMapping("/magasin")
public class MagasinResource {
    private final MagasinService magasinService;

    public MagasinResource(MagasinService magasinService) {
        this.magasinService = magasinService;
    }

    @GetMapping("/all")
    public ResponseEntity<List<Magasin>> getAllMagasin() {
        List<Magasin> magasins = magasinService.findAllMagasin();
        return new ResponseEntity<>(magasins, HttpStatus.OK);
    }

    @GetMapping("/find/{id}")
    public ResponseEntity<Magasin> getMagasinById(@PathVariable("id") Long id) {
        Magasin magasin = magasinService.findMagasinById(id);
        return new ResponseEntity<>(magasin, HttpStatus.OK);
    }

    @PostMapping("/add")
    public ResponseEntity<Magasin> addMagasin(@RequestBody Magasin magasin) {
        Magasin newMagasin = magasinService.addMagasin(magasin);
        return new ResponseEntity<>(newMagasin, HttpStatus.OK);
    }

    @PutMapping("/update")
    public ResponseEntity<Magasin> updateMagasin(@RequestBody Magasin magasin) {
        Magasin updatedMagasin = magasinService.updateMagasin(magasin);
        return new ResponseEntity<>(updatedMagasin, HttpStatus.OK);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Magasin> deleteMagasin(@PathVariable("id") Long id) {
        magasinService.deleteMagasin(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/delete/bulk")
    public ResponseEntity<?> deleteMagasins(@RequestBody List<Long> ids) {
        magasinService.deleteMagasins(ids);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
