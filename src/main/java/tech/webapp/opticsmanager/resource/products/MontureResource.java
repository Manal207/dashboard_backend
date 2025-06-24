package tech.webapp.opticsmanager.resource.products;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import tech.webapp.opticsmanager.model.products.Monture;
import tech.webapp.opticsmanager.model.products.Produit;
import tech.webapp.opticsmanager.service.products.MontureService;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/monture")
//@CrossOrigin(origins = "*") // Add this if your React app is on different port
public class MontureResource {

    private final MontureService montureService;

    @Value("${app.upload.dir:uploads/images}")
    private String uploadDir;

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

    // Simplified multipart endpoint - handles JSON data + file
    @PostMapping(value = "/add-with-image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Monture> addMontureWithImage(
            @RequestParam("montureData") String montureDataJson,
            @RequestParam(value = "image", required = false) MultipartFile imageFile) {

        try {
            // Parse the JSON data
            ObjectMapper objectMapper = new ObjectMapper();
            Monture monture = objectMapper.readValue(montureDataJson, Monture.class);

            // Handle image upload if provided
            if (imageFile != null && !imageFile.isEmpty()) {
                String imagePath = saveImageFile(imageFile);
                monture.setImage(imagePath);
            }

            // Set the product type
            monture.setTypeProduit(Produit.TypeProduit.MONTURE);

            Monture newMonture = montureService.addMonture(monture);
            return new ResponseEntity<>(newMonture, HttpStatus.CREATED);

        } catch (Exception e) {
            e.printStackTrace(); // This will show in console
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Helper method to save image
    private String saveImageFile(MultipartFile file) throws IOException {
        String originalFilename = StringUtils.cleanPath(file.getOriginalFilename());
        String fileExtension = originalFilename.substring(originalFilename.lastIndexOf("."));
        String newFilename = UUID.randomUUID().toString() + fileExtension;

        Path uploadPath = Paths.get("uploads/images");
        Files.createDirectories(uploadPath);
        Path filePath = uploadPath.resolve(newFilename);

        Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

        return newFilename;
    }

    // Alternative endpoint that accepts JSON + separate file upload
    @PostMapping("/add-json")
    public ResponseEntity<Monture> addMonture(@RequestBody Monture monture) {
        Monture newMonture = montureService.addMonture(monture);
        return new ResponseEntity<>(newMonture, HttpStatus.CREATED);
    }

    // Update with image
    @PutMapping(value = "/update", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Monture> updateMonture(
            @RequestParam("id") Long id,
            @RequestParam("marque") String marque,
            @RequestParam("reference") String reference,
            @RequestParam("couleur") String couleur,
            @RequestParam("branche") String branche,
            @RequestParam("forme") String forme,
            @RequestParam("genre") String genre,
            @RequestParam("matiere") String matiere,
            @RequestParam("typeMontage") String typeMontage,
            @RequestParam("gamme") String gamme,
            @RequestParam("quantiteInitiale") int quantiteInitiale,
            @RequestParam("numero") String numero,
            @RequestParam("nature") String nature,
            @RequestParam("prixAchat") String prixAchat,
            @RequestParam("prixVente") String prixVente,
            @RequestParam(value = "remiseClient", required = false) String remiseClient,
            @RequestParam(value = "image", required = false) MultipartFile imageFile) {

        try {
            Monture updatedMonture = montureService.updateMontureWithImage(
                    id, marque, reference, couleur, branche,
                    Monture.Forme.valueOf(forme),
                    Monture.Genre.valueOf(genre),
                    Monture.Matiere.valueOf(matiere),
                    Monture.TypeMontage.valueOf(typeMontage),
                    Monture.Gamme.valueOf(gamme),
                    quantiteInitiale, numero, nature,
                    prixAchat, prixVente, remiseClient, imageFile
            );
            return new ResponseEntity<>(updatedMonture, HttpStatus.OK);
        } catch (IOException e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    // Original update endpoint (without file)
    @PutMapping("/update-json")
    public ResponseEntity<Monture> updateMonture(@RequestBody Monture monture) {
        Monture updateMonture = montureService.updateMonture(monture);
        return new ResponseEntity<>(updateMonture, HttpStatus.OK);
    }

    // Enhanced image serving endpoint with debugging
    @GetMapping("/images/{filename}")
    @CrossOrigin(origins = "*")  // ← Make sure this line exists!
    public ResponseEntity<Resource> getImage(@PathVariable String filename) {
        try {
            Path filePath = Paths.get(uploadDir).resolve(filename).normalize();
            Resource resource = new UrlResource(filePath.toUri());

            if (resource.exists() && resource.isReadable()) {
                String contentType = "image/jpeg";
                if (filename.toLowerCase().endsWith(".png")) {
                    contentType = "image/png";
                } else if (filename.toLowerCase().endsWith(".gif")) {
                    contentType = "image/gif";
                }

                return ResponseEntity.ok()
                        .contentType(MediaType.parseMediaType(contentType))
                        .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + filename + "\"")
                        // Add these CORS headers manually as well
                        .header("Access-Control-Allow-Origin", "*")
                        .header("Access-Control-Allow-Methods", "GET")
                        .header("Access-Control-Allow-Headers", "*")
                        .body(resource);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).build();
        }
    }

    // Endpoint to serve images
//    @GetMapping("/images/{filename}")
//    public ResponseEntity<Resource> getImage(@PathVariable String filename) {
//        try {
//            Path filePath = Paths.get(uploadDir).resolve(filename).normalize();
//            Resource resource = new UrlResource(filePath.toUri());
//
//            if (resource.exists() && resource.isReadable()) {
//                String contentType = "image/jpeg"; // You might want to detect this
//                return ResponseEntity.ok()
//                        .contentType(MediaType.parseMediaType(contentType))
//                        .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + filename + "\"")
//                        .body(resource);
//            } else {
//                return ResponseEntity.notFound().build();
//            }
//        } catch (Exception e) {
//            return ResponseEntity.notFound().build();
//        }
//    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteMonture(@PathVariable("id") Long id) {
        montureService.deleteMonture(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/delete/bulk")
    public ResponseEntity<?> deleteMontures(@RequestBody List<Long> ids) {
        montureService.deleteMontures(ids);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}








//package tech.webapp.opticsmanager.resource.products;
//
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.core.io.Resource;
//import org.springframework.core.io.UrlResource;
//import org.springframework.http.HttpHeaders;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.MediaType;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//import org.springframework.web.multipart.MultipartFile;
//import tech.webapp.opticsmanager.model.products.Monture;
//import tech.webapp.opticsmanager.service.products.MontureService;
//
//import java.io.IOException;
//import java.nio.file.Path;
//import java.nio.file.Paths;
//import java.util.List;
//
//@RestController
//@RequestMapping("/monture")
//@CrossOrigin(origins = "*") // Add this if your React app is on different port
//public class MontureResource {
//
//    private final MontureService montureService;
//
//    @Value("${app.upload.dir:uploads/images}")
//    private String uploadDir;
//
//    public MontureResource(MontureService montureService) {
//        this.montureService = montureService;
//    }
//
//    @GetMapping("/all")
//    public ResponseEntity<List<Monture>> getAllMonture() {
//        List<Monture> montures = montureService.findAllMonture();
//        return new ResponseEntity<>(montures, HttpStatus.OK);
//    }
//
//    @GetMapping("/find/{id}")
//    public ResponseEntity<Monture> getMontureById(@PathVariable("id") Long id) {
//        Monture monture = montureService.findMontureById(id);
//        return new ResponseEntity<>(monture, HttpStatus.OK);
//    }
//
//    // Modified to handle both data and file upload
//    @PostMapping(value = "/add", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
//    public ResponseEntity<Monture> addMonture(
//            @RequestParam("marque") String marque,
//            @RequestParam("reference") String reference,
//            @RequestParam("couleur") String couleur,
//            @RequestParam("branche") String branche,
//            @RequestParam("forme") String forme,
//            @RequestParam("genre") String genre,
//            @RequestParam("matiere") String matiere,
//            @RequestParam("typeMontage") String typeMontage,
//            @RequestParam("gamme") String gamme,
//            @RequestParam("quantiteInitiale") int quantiteInitiale,
//            @RequestParam("numero") String numero,
//            @RequestParam("nature") String nature,
//            @RequestParam("prixAchat") String prixAchat,
//            @RequestParam("prixVente") String prixVente,
//            @RequestParam(value = "remiseClient", required = false) String remiseClient,
//            @RequestParam(value = "image", required = false) MultipartFile imageFile) {
//
//        try {
//            Monture newMonture = montureService.addMontureWithImage(
//                    marque, reference, couleur, branche,
//                    Monture.Forme.valueOf(forme),
//                    Monture.Genre.valueOf(genre),
//                    Monture.Matiere.valueOf(matiere),
//                    Monture.TypeMontage.valueOf(typeMontage),
//                    Monture.Gamme.valueOf(gamme),
//                    quantiteInitiale, numero, nature,
//                    prixAchat, prixVente, remiseClient, imageFile
//            );
//            return new ResponseEntity<>(newMonture, HttpStatus.CREATED);
//        } catch (IOException e) {
//            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
//        } catch (IllegalArgumentException e) {
//            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
//        }
//    }
//
//    // Alternative endpoint that accepts JSON + separate file upload
//    @PostMapping("/add-json")
//    public ResponseEntity<Monture> addMonture(@RequestBody Monture monture) {
//        Monture newMonture = montureService.addMonture(monture);
//        return new ResponseEntity<>(newMonture, HttpStatus.CREATED);
//    }
//
//    // Update with image
//    @PutMapping(value = "/update", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
//    public ResponseEntity<Monture> updateMonture(
//            @RequestParam("id") Long id,
//            @RequestParam("marque") String marque,
//            @RequestParam("reference") String reference,
//            @RequestParam("couleur") String couleur,
//            @RequestParam("branche") String branche,
//            @RequestParam("forme") String forme,
//            @RequestParam("genre") String genre,
//            @RequestParam("matiere") String matiere,
//            @RequestParam("typeMontage") String typeMontage,
//            @RequestParam("gamme") String gamme,
//            @RequestParam("quantiteInitiale") int quantiteInitiale,
//            @RequestParam("numero") String numero,
//            @RequestParam("nature") String nature,
//            @RequestParam("prixAchat") String prixAchat,
//            @RequestParam("prixVente") String prixVente,
//            @RequestParam(value = "remiseClient", required = false) String remiseClient,
//            @RequestParam(value = "image", required = false) MultipartFile imageFile) {
//
//        try {
//            Monture updatedMonture = montureService.updateMontureWithImage(
//                    id, marque, reference, couleur, branche,
//                    Monture.Forme.valueOf(forme),
//                    Monture.Genre.valueOf(genre),
//                    Monture.Matiere.valueOf(matiere),
//                    Monture.TypeMontage.valueOf(typeMontage),
//                    Monture.Gamme.valueOf(gamme),
//                    quantiteInitiale, numero, nature,
//                    prixAchat, prixVente, remiseClient, imageFile
//            );
//            return new ResponseEntity<>(updatedMonture, HttpStatus.OK);
//        } catch (IOException e) {
//            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
//        } catch (IllegalArgumentException e) {
//            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
//        }
//    }
//
//    // Original update endpoint (without file)
//    @PutMapping("/update-json")
//    public ResponseEntity<Monture> updateMonture(@RequestBody Monture monture) {
//        Monture updateMonture = montureService.updateMonture(monture);
//        return new ResponseEntity<>(updateMonture, HttpStatus.OK);
//    }
//
//    // Endpoint to serve images
//    @GetMapping("/images/{filename}")
//    public ResponseEntity<Resource> getImage(@PathVariable String filename) {
//        try {
//            Path filePath = Paths.get(uploadDir).resolve(filename).normalize();
//            Resource resource = new UrlResource(filePath.toUri());
//
//            if (resource.exists() && resource.isReadable()) {
//                String contentType = "image/jpeg"; // You might want to detect this
//                return ResponseEntity.ok()
//                        .contentType(MediaType.parseMediaType(contentType))
//                        .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + filename + "\"")
//                        .body(resource);
//            } else {
//                return ResponseEntity.notFound().build();
//            }
//        } catch (Exception e) {
//            return ResponseEntity.notFound().build();
//        }
//    }
//
//    @DeleteMapping("/delete/{id}")
//    public ResponseEntity<?> deleteMonture(@PathVariable("id") Long id) {
//        montureService.deleteMonture(id);
//        return new ResponseEntity<>(HttpStatus.OK);
//    }
//
//    @DeleteMapping("/delete/bulk")
//    public ResponseEntity<?> deleteMontures(@RequestBody List<Long> ids) {
//        montureService.deleteMontures(ids);
//        return new ResponseEntity<>(HttpStatus.OK);
//    }
//}
//
//
////package tech.webapp.opticsmanager.resource.products;
////
////import org.springframework.beans.factory.annotation.Value;
////import org.springframework.core.io.Resource;
////import org.springframework.core.io.UrlResource;
////import org.springframework.http.HttpHeaders;
////import org.springframework.http.HttpStatus;
////import org.springframework.http.MediaType;
////import org.springframework.http.ResponseEntity;
////import org.springframework.web.bind.annotation.*;
////import org.springframework.web.multipart.MultipartFile;
////import tech.webapp.opticsmanager.model.products.Monture;
////import tech.webapp.opticsmanager.service.products.MontureService;
////
////import java.io.IOException;
////import java.nio.file.Path;
////import java.nio.file.Paths;
////import java.util.List;
////
////@RestController
////@RequestMapping("/monture")
////@CrossOrigin(origins = "*") // Add this if your React app is on different port
////public class MontureResource {
////
////    private final MontureService montureService;
////
////    @Value("${app.upload.dir:uploads/images}")
////    private String uploadDir;
////
////    public MontureResource(MontureService montureService) {
////        this.montureService = montureService;
////    }
////
////    @GetMapping("/all")
////    public ResponseEntity<List<Monture>> getAllMonture() {
////        List<Monture> montures = montureService.findAllMonture();
////        return new ResponseEntity<>(montures, HttpStatus.OK);
////    }
////
////    @GetMapping("/find/{id}")
////    public ResponseEntity<Monture> getMontureById(@PathVariable("id") Long id) {
////        Monture monture = montureService.findMontureById(id);
////        return new ResponseEntity<>(monture, HttpStatus.OK);
////    }
////
////    // Modified to handle both data and file upload
////    @PostMapping(value = "/add", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
////    public ResponseEntity<Monture> addMonture(
////            @RequestParam("marque") String marque,
////            @RequestParam("reference") String reference,
////            @RequestParam("couleur") String couleur,
////            @RequestParam("branche") String branche,
////            @RequestParam("forme") Monture.Forme forme,
////            @RequestParam("genre") Monture.Genre genre,
////            @RequestParam("matiere") Monture.Matiere matiere,
////            @RequestParam("typeMontage") Monture.TypeMontage typeMontage,
////            @RequestParam("gamme") Monture.Gamme gamme,
////            @RequestParam("quantiteInitiale") int quantiteInitiale,
////            @RequestParam("numero") String numero,
////            @RequestParam("nature") String nature,
////            @RequestParam("prixAchat") String prixAchat,
////            @RequestParam("prixVente") String prixVente,
////            @RequestParam(value = "remiseClient", required = false) String remiseClient,
////            @RequestParam(value = "image", required = false) MultipartFile imageFile) {
////
////        try {
////            Monture newMonture = montureService.addMontureWithImage(
////                    marque, reference, couleur, branche, forme, genre, matiere,
////                    typeMontage, gamme, quantiteInitiale, numero, nature,
////                    prixAchat, prixVente, remiseClient, imageFile
////            );
////            return new ResponseEntity<>(newMonture, HttpStatus.CREATED);
////        } catch (IOException e) {
////            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
////        }
////    }
////
////    // Alternative endpoint that accepts JSON + separate file upload
////    @PostMapping("/add-json")
////    public ResponseEntity<Monture> addMonture(@RequestBody Monture monture) {
////        Monture newMonture = montureService.addMonture(monture);
////        return new ResponseEntity<>(newMonture, HttpStatus.CREATED);
////    }
////
////    // Update with image
////    @PutMapping(value = "/update", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
////    public ResponseEntity<Monture> updateMonture(
////            @RequestParam("id") Long id,
////            @RequestParam("marque") String marque,
////            @RequestParam("reference") String reference,
////            @RequestParam("couleur") String couleur,
////            @RequestParam("branche") String branche,
////            @RequestParam("forme") Monture.Forme forme,
////            @RequestParam("genre") Monture.Genre genre,
////            @RequestParam("matiere") Monture.Matiere matiere,
////            @RequestParam("typeMontage") Monture.TypeMontage typeMontage,
////            @RequestParam("gamme") Monture.Gamme gamme,
////            @RequestParam("quantiteInitiale") int quantiteInitiale,
////            @RequestParam("numero") String numero,
////            @RequestParam("nature") String nature,
////            @RequestParam("prixAchat") String prixAchat,
////            @RequestParam("prixVente") String prixVente,
////            @RequestParam(value = "remiseClient", required = false) String remiseClient,
////            @RequestParam(value = "image", required = false) MultipartFile imageFile) {
////
////        try {
////            Monture updatedMonture = montureService.updateMontureWithImage(
////                    id, marque, reference, couleur, branche, forme, genre, matiere,
////                    typeMontage, gamme, quantiteInitiale, numero, nature,
////                    prixAchat, prixVente, remiseClient, imageFile
////            );
////            return new ResponseEntity<>(updatedMonture, HttpStatus.OK);
////        } catch (IOException e) {
////            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
////        }
////    }
////
////    // Original update endpoint (without file)
////    @PutMapping("/update-json")
////    public ResponseEntity<Monture> updateMonture(@RequestBody Monture monture) {
////        Monture updateMonture = montureService.updateMonture(monture);
////        return new ResponseEntity<>(updateMonture, HttpStatus.OK);
////    }
////
////    // Endpoint to serve images
////    @GetMapping("/images/{filename}")
////    public ResponseEntity<Resource> getImage(@PathVariable String filename) {
////        try {
////            Path filePath = Paths.get(uploadDir).resolve(filename).normalize();
////            Resource resource = new UrlResource(filePath.toUri());
////
////            if (resource.exists() && resource.isReadable()) {
////                String contentType = "image/jpeg"; // You might want to detect this
////                return ResponseEntity.ok()
////                        .contentType(MediaType.parseMediaType(contentType))
////                        .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + filename + "\"")
////                        .body(resource);
////            } else {
////                return ResponseEntity.notFound().build();
////            }
////        } catch (Exception e) {
////            return ResponseEntity.notFound().build();
////        }
////    }
////
////    @DeleteMapping("/delete/{id}")
////    public ResponseEntity<?> deleteMonture(@PathVariable("id") Long id) {
////        montureService.deleteMonture(id);
////        return new ResponseEntity<>(HttpStatus.OK);
////    }
////
////    @DeleteMapping("/delete/bulk")
////    public ResponseEntity<?> deleteMontures(@RequestBody List<Long> ids) {
////        montureService.deleteMontures(ids);
////        return new ResponseEntity<>(HttpStatus.OK);
////    }
////}
////
////
////
////
////
////
////
//////package tech.webapp.opticsmanager.resource.products;
//////
//////
//////import lombok.Value;
//////import org.springframework.core.io.Resource;
//////import org.springframework.core.io.UrlResource;
//////import org.springframework.http.HttpHeaders;
//////import org.springframework.http.HttpStatus;
//////import org.springframework.http.MediaType;
//////import org.springframework.http.ResponseEntity;
//////import org.springframework.web.bind.annotation.*;
//////import org.springframework.web.multipart.MultipartFile;
//////import tech.webapp.opticsmanager.model.products.Monture;
//////import tech.webapp.opticsmanager.service.products.MontureService;
//////
//////import java.io.IOException;
//////import java.nio.file.Path;
//////import java.nio.file.Paths;
//////import java.util.List;
//////
//////@RestController
//////@RequestMapping("/monture")
//////
//////public class MontureResource {
//////    private final MontureService montureService;
//////    public MontureResource(MontureService montureService) {
//////        this.montureService = montureService;
//////    }
//////
//////
//////    @Value("${app.upload.dir:uploads/images}")
//////    private String uploadDir;
//////
//////    @GetMapping("/all")
//////    public ResponseEntity<List<Monture>> getAllMonture() {
//////        List<Monture> montures = montureService.findAllMonture();
//////        return new ResponseEntity<>(montures, HttpStatus.OK);
//////
//////    }
//////
//////    @GetMapping("/find/{id}")
//////    public ResponseEntity<Monture> getMontureById(@PathVariable("id") Long id) {
//////        Monture monture = montureService.findMontureById(id);
//////        return new ResponseEntity<>(monture, HttpStatus.OK);
//////    }
//////
////////    @PostMapping("/add")
////////    public ResponseEntity<Monture> addMonture(@RequestBody Monture monture) {
////////        Monture newMonture = montureService.addMonture(monture);
////////        return new ResponseEntity<>(newMonture, HttpStatus.OK);
////////    }
////////
////////    @PutMapping("/update")
////////    public ResponseEntity<Monture> updateMonture(@RequestBody Monture monture) {
////////        Monture updateMonture = montureService.updateMonture(monture);
////////        return new ResponseEntity<>(updateMonture, HttpStatus.OK);
////////    }
//////
//////    // Modified to handle both data and file upload
//////    @PostMapping(value = "/add", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
//////    public ResponseEntity<Monture> addMonture(
//////            @RequestParam("marque") String marque,
//////            @RequestParam("reference") String reference,
//////            @RequestParam("couleur") String couleur,
//////            @RequestParam("branche") String branche,
//////            @RequestParam("forme") Monture.Forme forme,
//////            @RequestParam("genre") Monture.Genre genre,
//////            @RequestParam("matiere") Monture.Matiere matiere,
//////            @RequestParam("typeMontage") Monture.TypeMontage typeMontage,
//////            @RequestParam("gamme") Monture.Gamme gamme,
//////            @RequestParam("quantiteInitiale") int quantiteInitiale,
//////            @RequestParam("numero") String numero,
//////            @RequestParam("nature") String nature,
//////            @RequestParam("prixAchat") String prixAchat,
//////            @RequestParam("prixVente") String prixVente,
//////            @RequestParam(value = "remiseClient", required = false) String remiseClient,
//////            @RequestParam(value = "image", required = false) MultipartFile imageFile) {
//////
//////        try {
//////            Monture newMonture = montureService.addMontureWithImage(
//////                    marque, reference, couleur, branche, forme, genre, matiere,
//////                    typeMontage, gamme, quantiteInitiale, numero, nature,
//////                    prixAchat, prixVente, remiseClient, imageFile
//////            );
//////            return new ResponseEntity<>(newMonture, HttpStatus.CREATED);
//////        } catch (IOException e) {
//////            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
//////        }
//////    }
//////
//////    // Alternative endpoint that accepts JSON + separate file upload
//////    @PostMapping("/add-json")
//////    public ResponseEntity<Monture> addMonture(@RequestBody Monture monture) {
//////        Monture newMonture = montureService.addMonture(monture);
//////        return new ResponseEntity<>(newMonture, HttpStatus.CREATED);
//////    }
//////
//////    // Update with image
//////    @PutMapping(value = "/update", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
//////    public ResponseEntity<Monture> updateMonture(
//////            @RequestParam("id") Long id,
//////            @RequestParam("marque") String marque,
//////            @RequestParam("reference") String reference,
//////            @RequestParam("couleur") String couleur,
//////            @RequestParam("branche") String branche,
//////            @RequestParam("forme") Monture.Forme forme,
//////            @RequestParam("genre") Monture.Genre genre,
//////            @RequestParam("matiere") Monture.Matiere matiere,
//////            @RequestParam("typeMontage") Monture.TypeMontage typeMontage,
//////            @RequestParam("gamme") Monture.Gamme gamme,
//////            @RequestParam("quantiteInitiale") int quantiteInitiale,
//////            @RequestParam("numero") String numero,
//////            @RequestParam("nature") String nature,
//////            @RequestParam("prixAchat") String prixAchat,
//////            @RequestParam("prixVente") String prixVente,
//////            @RequestParam(value = "remiseClient", required = false) String remiseClient,
//////            @RequestParam(value = "image", required = false) MultipartFile imageFile) {
//////
//////        try {
//////            Monture updatedMonture = montureService.updateMontureWithImage(
//////                    id, marque, reference, couleur, branche, forme, genre, matiere,
//////                    typeMontage, gamme, quantiteInitiale, numero, nature,
//////                    prixAchat, prixVente, remiseClient, imageFile
//////            );
//////            return new ResponseEntity<>(updatedMonture, HttpStatus.OK);
//////        } catch (IOException e) {
//////            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
//////        }
//////    }
//////
//////    // Original update endpoint (without file)
//////    @PutMapping("/update-json")
//////    public ResponseEntity<Monture> updateMonture(@RequestBody Monture monture) {
//////        Monture updateMonture = montureService.updateMonture(monture);
//////        return new ResponseEntity<>(updateMonture, HttpStatus.OK);
//////    }
//////
//////    // Endpoint to serve images
//////    @GetMapping("/images/{filename}")
//////    public ResponseEntity<Resource> getImage(@PathVariable String filename) {
//////        try {
//////            Path filePath = Paths.get(uploadDir).resolve(filename).normalize();
//////            Resource resource = new UrlResource(filePath.toUri());
//////
//////            if (resource.exists() && resource.isReadable()) {
//////                String contentType = "image/jpeg"; // You might want to detect this
//////                return ResponseEntity.ok()
//////                        .contentType(MediaType.parseMediaType(contentType))
//////                        .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + filename + "\"")
//////                        .body(resource);
//////            } else {
//////                return ResponseEntity.notFound().build();
//////            }
//////        } catch (Exception e) {
//////            return ResponseEntity.notFound().build();
//////        }
//////    }
//////
//////
//////    @DeleteMapping("/delete/{id}")
//////    public ResponseEntity<Monture> deleteMonture(@PathVariable("id") Long id) {
//////        montureService.deleteMonture(id);
//////        return new ResponseEntity<>(HttpStatus.OK);
//////    }
//////
//////
//////
//////    @DeleteMapping("/delete/bulk")
//////    public ResponseEntity<?> deleteMontures(@RequestBody List<Long> ids) {
//////        montureService.deleteMontures(ids);
//////        return new ResponseEntity<>(HttpStatus.OK);
//////    }
//////
//////
//////
//////}
