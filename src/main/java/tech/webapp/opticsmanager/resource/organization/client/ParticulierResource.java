package tech.webapp.opticsmanager.resource.organization.client;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.webapp.opticsmanager.model.organization.client.Particulier;
import tech.webapp.opticsmanager.service.organization.client.ParticulierService;

import java.util.List;

@RestController
@RequestMapping("/particulier")
public class ParticulierResource {
    private final ParticulierService particulierService;

    public ParticulierResource(ParticulierService particulierService) {
        this.particulierService = particulierService;
    }

    @GetMapping("/all")
    public ResponseEntity<List<Particulier>> getAllParticulier() {
        List<Particulier> particuliers = particulierService.findAllParticulier();
        return new ResponseEntity<>(particuliers, HttpStatus.OK);
    }

    @GetMapping("/find/{id}")
    public ResponseEntity<Particulier> getParticulierById(@PathVariable("id") Long id) {
        Particulier particulier = particulierService.findParticulierById(id);
        return new ResponseEntity<>(particulier, HttpStatus.OK);
    }

    @PostMapping("/add")
    public ResponseEntity<Particulier> addParticulier(@RequestBody Particulier particulier) {
        Particulier newParticulier = particulierService.addParticulier(particulier);
        return new ResponseEntity<>(newParticulier, HttpStatus.OK);
    }

    @PutMapping("/update")
    public ResponseEntity<Particulier> updateParticulier(@RequestBody Particulier particulier) {
        Particulier updatedParticulier = particulierService.updateParticulier(particulier);
        return new ResponseEntity<>(updatedParticulier, HttpStatus.OK);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Particulier> deleteParticulier(@PathVariable("id") Long id) {
        particulierService.deleteParticulier(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/delete/bulk")
    public ResponseEntity<?> deleteParticuliers(@RequestBody List<Long> ids) {
        particulierService.deleteParticuliers(ids);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
