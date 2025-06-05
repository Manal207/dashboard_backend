package tech.webapp.opticsmanager.resource.products;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.webapp.opticsmanager.model.products.Lentille;
import tech.webapp.opticsmanager.service.products.LentilleService;

import java.util.List;

@RestController
@RequestMapping("/lentille")

public class LentilleResource {
    private final LentilleService lentilleService;
    public LentilleResource(LentilleService lentilleService) {
        this.lentilleService = lentilleService;
    }

    @GetMapping("/all")
    public ResponseEntity<List<Lentille>> getAllLentille() {
        List<Lentille> lentilles = lentilleService.findAllLentille();
        return new ResponseEntity<>(lentilles, HttpStatus.OK);
    }

    @GetMapping("/find/{id}")
    public ResponseEntity<Lentille> getLentilleById(@PathVariable("id") Long id) {
        Lentille lentille = lentilleService.findLentilleById(id);
        return new ResponseEntity<>(lentille, HttpStatus.OK);
    }

    @PostMapping("/add")
    public ResponseEntity<Lentille> addLentille(@RequestBody Lentille lentille) {
        Lentille newLentille = lentilleService.addLentille(lentille);
        return new ResponseEntity<>(newLentille, HttpStatus.OK);
    }

    @PutMapping("/update")
    public ResponseEntity<Lentille> updateLentille(@RequestBody Lentille lentille) {
        Lentille updateLentille = lentilleService.updateLentille(lentille);
        return new ResponseEntity<>(updateLentille, HttpStatus.OK);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Lentille> deleteLentille(@PathVariable("id") Long id) {
        lentilleService.deleteLentille(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/delete/bulk")
    public ResponseEntity<?> deleteLentilles(@RequestBody List<Long> ids) {
        lentilleService.deleteLentilles(ids);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}