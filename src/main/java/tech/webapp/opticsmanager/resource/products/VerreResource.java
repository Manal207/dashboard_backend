package tech.webapp.opticsmanager.resource.products;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.webapp.opticsmanager.model.products.Verre;
import tech.webapp.opticsmanager.service.products.VerreService;

import java.util.List;

@RestController
@RequestMapping("/verre")
public class VerreResource {
    private final VerreService verreService;

    public VerreResource(VerreService verreService) {
        this.verreService = verreService;
    }

    @GetMapping("/all")
    public ResponseEntity<List<Verre>> getAllVerre() {
        List<Verre> verres = verreService.findAllVerre();
        return new ResponseEntity<>(verres, HttpStatus.OK);
    }

    @GetMapping("/find/{id}")
    public ResponseEntity<Verre> getVerreById(@PathVariable("id") Long id) {
        Verre verre = verreService.findVerreById(id);
        return new ResponseEntity<>(verre, HttpStatus.OK);
    }

    @PostMapping("/add")
    public ResponseEntity<Verre> addVerre(@RequestBody Verre verre) {
        Verre newVerre = verreService.addVerre(verre);
        return new ResponseEntity<>(newVerre, HttpStatus.OK);
    }

    @PutMapping("/update")
    public ResponseEntity<Verre> updateVerre(@RequestBody Verre verre) {
        Verre updateVerre = verreService.updateVerre(verre);
        return new ResponseEntity<>(updateVerre, HttpStatus.OK);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Verre> deleteVerre(@PathVariable("id") Long id) {
        verreService.deleteVerre(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/delete/bulk")
    public ResponseEntity<?> deleteVerres(@RequestBody List<Long> ids) {
        verreService.deleteVerres(ids);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
