package tech.webapp.opticsmanager.resource.products;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.webapp.opticsmanager.model.products.Accessoire;
import tech.webapp.opticsmanager.service.products.AccessoireService;

import java.util.List;

@RestController
@RequestMapping("/accessoire")
public class AccessoireResource {
    private final AccessoireService accessoireService;

    public AccessoireResource(AccessoireService accessoireService) {
        this.accessoireService = accessoireService;
    }

    @GetMapping("/all")
    public ResponseEntity<List<Accessoire>> getAllAccessoires() {
        List<Accessoire> accessoires = accessoireService.findAllAccessoire();
        return new ResponseEntity<>(accessoires, HttpStatus.OK);
    }

    @GetMapping("/find/{id}")
    public ResponseEntity<Accessoire> getAccessoireById(@PathVariable("id") Long id) {
        Accessoire accessoire = accessoireService.findAccessoireById(id);
        return new ResponseEntity<>(accessoire, HttpStatus.OK);
    }

    @PostMapping("/add")
    public ResponseEntity<Accessoire> addAccessoire(@RequestBody Accessoire accessoire) {
        Accessoire newAccessoire = accessoireService.addAccessoire(accessoire);
        return new ResponseEntity<>(newAccessoire, HttpStatus.OK);
    }

    @PutMapping("/update")
    public ResponseEntity<Accessoire> updateAccessoire(@RequestBody Accessoire accessoire) {
        Accessoire updatedAccessoire = accessoireService.updateAccessoire(accessoire);
        return new ResponseEntity<>(updatedAccessoire, HttpStatus.OK);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteAccessoire(@PathVariable("id") Long id) {
        accessoireService.deleteAccessoire(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/delete/bulk")
    public ResponseEntity<?> deleteAccessoires(@RequestBody List<Long> ids) {
        accessoireService.deleteAccessoires(ids);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
