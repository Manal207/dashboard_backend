package tech.webapp.opticsmanager.resource.products;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.webapp.opticsmanager.model.products.Monture;
import tech.webapp.opticsmanager.service.products.MontureService;

import java.util.List;

@RestController
@RequestMapping("/monture")

public class MontureResource {
    private final MontureService montureService;
    public MontureResource(MontureService montureService) {
        this.montureService = montureService;
    }

    @GetMapping("/all")
    public ResponseEntity<List<Monture>> getAllMonture() {
        List<Monture> montures = montureService.findAllMonture();
        return new ResponseEntity<>(montures, HttpStatus.OK);

    }

    @GetMapping("/find/{id}")
    public ResponseEntity<Monture> getMontureById(@PathVariable("id") Long id) {
        Monture monture = montureService.findMontureById(id);
        return new ResponseEntity<>(monture, HttpStatus.OK);
    }

    @PostMapping("/add")
    public ResponseEntity<Monture> addMonture(@RequestBody Monture monture) {
        Monture newMonture = montureService.addMonture(monture);
        return new ResponseEntity<>(newMonture, HttpStatus.OK);
    }

    @PutMapping("/update")
    public ResponseEntity<Monture> updateMonture(@RequestBody Monture monture) {
        Monture updateMonture = montureService.updateMonture(monture);
        return new ResponseEntity<>(updateMonture, HttpStatus.OK);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Monture> deleteMonture(@PathVariable("id") Long id) {
        montureService.deleteMonture(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }



    @DeleteMapping("/delete/bulk")
    public ResponseEntity<?> deleteMontures(@RequestBody List<Long> ids) {
        montureService.deleteMontures(ids);
        return new ResponseEntity<>(HttpStatus.OK);
    }



}
