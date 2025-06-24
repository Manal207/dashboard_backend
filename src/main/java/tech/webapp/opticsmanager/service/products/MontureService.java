package tech.webapp.opticsmanager.service.products;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import tech.webapp.opticsmanager.exception.MontureNotFoundException;
import tech.webapp.opticsmanager.model.products.Monture;
import tech.webapp.opticsmanager.model.products.Produit;
import tech.webapp.opticsmanager.repo.products.MontureRepo;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.UUID;

@Service
public class MontureService {

    private final MontureRepo montureRepo;

    @Value("${app.upload.dir:uploads/images}")
    private String uploadDir;

    @Autowired
    public MontureService(MontureRepo montureRepo) {
        this.montureRepo = montureRepo;
    }

    public Monture addMonture(Monture monture) {
        return montureRepo.save(monture);
    }

    // New method to handle monture creation with image upload
    public Monture addMontureWithImage(String marque, String reference, String couleur,
                                       String branche, Monture.Forme forme, Monture.Genre genre,
                                       Monture.Matiere matiere, Monture.TypeMontage typeMontage,
                                       Monture.Gamme gamme, int quantiteInitiale, String numero,
                                       String nature, String prixAchat, String prixVente,
                                       String remiseClient, MultipartFile imageFile) throws IOException {

        Monture monture = new Monture();

        // Set Monture specific fields
        monture.setMarque(marque);
        monture.setReference(reference);
        monture.setCouleur(couleur);
        monture.setBranche(branche);
        monture.setForme(forme);
        monture.setGenre(genre);
        monture.setMatiere(matiere);
        monture.setTypeMontage(typeMontage);
        monture.setGamme(gamme);
        monture.setQuantiteInitiale(quantiteInitiale);
        monture.setNumero(numero);

        // Set Produit inherited fields
        monture.setNature(nature);
        monture.setPrixAchat(new BigDecimal(prixAchat));
        monture.setPrixVente(new BigDecimal(prixVente));
        if (remiseClient != null && !remiseClient.isEmpty()) {
            monture.setRemiseClient(Double.parseDouble(remiseClient));
        }
        monture.setTypeProduit(Produit.TypeProduit.MONTURE);

        // Handle image upload
        if (imageFile != null && !imageFile.isEmpty()) {
            String imagePath = saveImage(imageFile);
            monture.setImage(imagePath);
        }

        return montureRepo.save(monture);
    }

    // Method to update monture with image
    public Monture updateMontureWithImage(Long id, String marque, String reference, String couleur,
                                          String branche, Monture.Forme forme, Monture.Genre genre,
                                          Monture.Matiere matiere, Monture.TypeMontage typeMontage,
                                          Monture.Gamme gamme, int quantiteInitiale, String numero,
                                          String nature, String prixAchat, String prixVente,
                                          String remiseClient, MultipartFile imageFile) throws IOException {

        Monture existingMonture = findMontureById(id);

        // Update Monture specific fields
        existingMonture.setMarque(marque);
        existingMonture.setReference(reference);
        existingMonture.setCouleur(couleur);
        existingMonture.setBranche(branche);
        existingMonture.setForme(forme);
        existingMonture.setGenre(genre);
        existingMonture.setMatiere(matiere);
        existingMonture.setTypeMontage(typeMontage);
        existingMonture.setGamme(gamme);
        existingMonture.setQuantiteInitiale(quantiteInitiale);
        existingMonture.setNumero(numero);

        // Update Produit inherited fields
        existingMonture.setNature(nature);
        existingMonture.setPrixAchat(new BigDecimal(prixAchat));
        existingMonture.setPrixVente(new BigDecimal(prixVente));
        if (remiseClient != null && !remiseClient.isEmpty()) {
            existingMonture.setRemiseClient(Double.parseDouble(remiseClient));
        }

        // Handle image upload
        if (imageFile != null && !imageFile.isEmpty()) {
            // Delete old image if exists
            if (existingMonture.getImage() != null) {
                deleteImage(existingMonture.getImage());
            }
            String imagePath = saveImage(imageFile);
            existingMonture.setImage(imagePath);
        }

        return montureRepo.save(existingMonture);
    }

    public Monture updateMonture(Monture monture) {
        return montureRepo.save(monture);
    }

    @Transactional
    public void deleteMonture(Long id) {
        Monture monture = findMontureById(id);
        // Delete associated image file
        if (monture.getImage() != null) {
            deleteImage(monture.getImage());
        }
        montureRepo.deleteMontureById(id);
    }

    public List<Monture> findAllMonture() {
        return montureRepo.findAll();
    }

    public Monture findMontureById(Long id) {
        return montureRepo.findMontureById(id)
                .orElseThrow(() -> new MontureNotFoundException("Monture by id not found"));
    }

    public void deleteMontures(List<Long> ids) {
        // Delete associated images
        List<Monture> montures = montureRepo.findAllById(ids);
        for (Monture monture : montures) {
            if (monture.getImage() != null) {
                deleteImage(monture.getImage());
            }
        }
        montureRepo.deleteAllByIdInBatch(ids);
    }

    // Helper method to save image file
    private String saveImage(MultipartFile file) throws IOException {
        String originalFilename = StringUtils.cleanPath(file.getOriginalFilename());
        String fileExtension = originalFilename.substring(originalFilename.lastIndexOf("."));
        String newFilename = UUID.randomUUID().toString() + fileExtension;

        Path uploadPath = Paths.get(uploadDir);
        // Create directory if it doesn't exist
        Files.createDirectories(uploadPath);
        Path filePath = uploadPath.resolve(newFilename);

        Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

        return newFilename; // Return just the filename, not the full path
    }

    // Helper method to delete image file
    private void deleteImage(String filename) {
        try {
            Path filePath = Paths.get(uploadDir).resolve(filename);
            Files.deleteIfExists(filePath);
        } catch (IOException e) {
            // Log the error but don't throw exception
            System.err.println("Could not delete image file: " + filename);
        }
    }
}








//package tech.webapp.opticsmanager.service.products;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//import org.springframework.util.StringUtils;
//import org.springframework.web.multipart.MultipartFile;
//import tech.webapp.opticsmanager.exception.MontureNotFoundException;
//import tech.webapp.opticsmanager.model.products.Monture;
//import tech.webapp.opticsmanager.model.products.Produit;
//import tech.webapp.opticsmanager.repo.products.MontureRepo;
//
//import java.io.IOException;
//import java.math.BigDecimal;
//import java.nio.file.Files;
//import java.nio.file.Path;
//import java.nio.file.Paths;
//import java.nio.file.StandardCopyOption;
//import java.util.List;
//import java.util.UUID;
//
//@Service
//public class MontureService {
//
//    private final MontureRepo montureRepo;
//
//    @Value("${app.upload.dir:uploads/images}")
//    private String uploadDir;
//
//    @Autowired
//    public MontureService(MontureRepo montureRepo) {
//        this.montureRepo = montureRepo;
//        // Create upload directory if it doesn't exist
//        try {
//            Files.createDirectories(Paths.get(uploadDir));
//        } catch (IOException e) {
//            throw new RuntimeException("Could not create upload directory!", e);
//        }
//    }
//
//    public Monture addMonture(Monture monture) {
//        return montureRepo.save(monture);
//    }
//
//    // New method to handle monture creation with image upload
//    public Monture addMontureWithImage(String marque, String reference, String couleur,
//                                       String branche, Monture.Forme forme, Monture.Genre genre,
//                                       Monture.Matiere matiere, Monture.TypeMontage typeMontage,
//                                       Monture.Gamme gamme, int quantiteInitiale, String numero,
//                                       String nature, String prixAchat, String prixVente,
//                                       String remiseClient, MultipartFile imageFile) throws IOException {
//
//        Monture monture = new Monture();
//
//        // Set Monture specific fields
//        monture.setMarque(marque);
//        monture.setReference(reference);
//        monture.setCouleur(couleur);
//        monture.setBranche(branche);
//        monture.setForme(forme);
//        monture.setGenre(genre);
//        monture.setMatiere(matiere);
//        monture.setTypeMontage(typeMontage);
//        monture.setGamme(gamme);
//        monture.setQuantiteInitiale(quantiteInitiale);
//        monture.setNumero(numero);
//
//        // Set Produit inherited fields
//        monture.setNature(nature);
//        monture.setPrixAchat(new BigDecimal(prixAchat));
//        monture.setPrixVente(new BigDecimal(prixVente));
//        if (remiseClient != null && !remiseClient.isEmpty()) {
//            monture.setRemiseClient(Double.parseDouble(remiseClient));
//        }
//        monture.setTypeProduit(Produit.TypeProduit.MONTURE);
//
//        // Handle image upload
//        if (imageFile != null && !imageFile.isEmpty()) {
//            String imagePath = saveImage(imageFile);
//            monture.setImage(imagePath);
//        }
//
//        return montureRepo.save(monture);
//    }
//
//    // Method to update monture with image
//    public Monture updateMontureWithImage(Long id, String marque, String reference, String couleur,
//                                          String branche, Monture.Forme forme, Monture.Genre genre,
//                                          Monture.Matiere matiere, Monture.TypeMontage typeMontage,
//                                          Monture.Gamme gamme, int quantiteInitiale, String numero,
//                                          String nature, String prixAchat, String prixVente,
//                                          String remiseClient, MultipartFile imageFile) throws IOException {
//
//        Monture existingMonture = findMontureById(id);
//
//        // Update Monture specific fields
//        existingMonture.setMarque(marque);
//        existingMonture.setReference(reference);
//        existingMonture.setCouleur(couleur);
//        existingMonture.setBranche(branche);
//        existingMonture.setForme(forme);
//        existingMonture.setGenre(genre);
//        existingMonture.setMatiere(matiere);
//        existingMonture.setTypeMontage(typeMontage);
//        existingMonture.setGamme(gamme);
//        existingMonture.setQuantiteInitiale(quantiteInitiale);
//        existingMonture.setNumero(numero);
//
//        // Update Produit inherited fields
//        existingMonture.setNature(nature);
//        existingMonture.setPrixAchat(new BigDecimal(prixAchat));
//        existingMonture.setPrixVente(new BigDecimal(prixVente));
//        if (remiseClient != null && !remiseClient.isEmpty()) {
//            existingMonture.setRemiseClient(Double.parseDouble(remiseClient));
//        }
//
//        // Handle image upload
//        if (imageFile != null && !imageFile.isEmpty()) {
//            // Delete old image if exists
//            if (existingMonture.getImage() != null) {
//                deleteImage(existingMonture.getImage());
//            }
//            String imagePath = saveImage(imageFile);
//            existingMonture.setImage(imagePath);
//        }
//
//        return montureRepo.save(existingMonture);
//    }
//
//    public Monture updateMonture(Monture monture) {
//        return montureRepo.save(monture);
//    }
//
//    @Transactional
//    public void deleteMonture(Long id) {
//        Monture monture = findMontureById(id);
//        // Delete associated image file
//        if (monture.getImage() != null) {
//            deleteImage(monture.getImage());
//        }
//        montureRepo.deleteMontureById(id);
//    }
//
//    public List<Monture> findAllMonture() {
//        return montureRepo.findAll();
//    }
//
//    public Monture findMontureById(Long id) {
//        return montureRepo.findMontureById(id)
//                .orElseThrow(() -> new MontureNotFoundException("Monture by id not found"));
//    }
//
//    public void deleteMontures(List<Long> ids) {
//        // Delete associated images
//        List<Monture> montures = montureRepo.findAllById(ids);
//        for (Monture monture : montures) {
//            if (monture.getImage() != null) {
//                deleteImage(monture.getImage());
//            }
//        }
//        montureRepo.deleteAllByIdInBatch(ids);
//    }
//
//    // Helper method to save image file
//    private String saveImage(MultipartFile file) throws IOException {
//        String originalFilename = StringUtils.cleanPath(file.getOriginalFilename());
//        String fileExtension = originalFilename.substring(originalFilename.lastIndexOf("."));
//        String newFilename = UUID.randomUUID().toString() + fileExtension;
//
//        Path uploadPath = Paths.get(uploadDir);
//        Path filePath = uploadPath.resolve(newFilename);
//
//        Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
//
//        return newFilename; // Return just the filename, not the full path
//    }
//
//    // Helper method to delete image file
//    private void deleteImage(String filename) {
//        try {
//            Path filePath = Paths.get(uploadDir).resolve(filename);
//            Files.deleteIfExists(filePath);
//        } catch (IOException e) {
//            // Log the error but don't throw exception
//            System.err.println("Could not delete image file: " + filename);
//        }
//    }
//}
//
//
//
//
////package tech.webapp.opticsmanager.service.products;
////
////import org.springframework.beans.factory.annotation.Autowired;
////import org.springframework.stereotype.Service;
////import org.springframework.transaction.annotation.Transactional;
////import tech.webapp.opticsmanager.exception.MontureNotFoundException;
////import tech.webapp.opticsmanager.model.products.Monture;
////import tech.webapp.opticsmanager.repo.products.MontureRepo;
////
////import java.util.List;
////
////@Service
////public class MontureService {
////    private final MontureRepo montureRepo;
////
////    @Autowired
////    public MontureService(MontureRepo montureRepo) {
////        this.montureRepo = montureRepo;
////    }
////
////    public Monture addMonture(Monture monture) {
////        return montureRepo.save(monture);
////    }
////
////    public Monture updateMonture(Monture monture) {
////        return montureRepo.save(monture);
////    }
////
////    @Transactional
////    public void deleteMonture(Long id) {
////        montureRepo.deleteMontureById(id);
////    }
////
////    public List<Monture> findAllMonture() {
////        return montureRepo.findAll();
////    }
////    public Monture findMontureById(Long id) {
////        return montureRepo.findMontureById(id)
////                .orElseThrow(()-> new MontureNotFoundException("Monture by id not found"));
////    }
////
////
////    public void deleteMontures(List<Long> ids) {
////        montureRepo.deleteAllByIdInBatch(ids);
////    }
////
////
////
////}
