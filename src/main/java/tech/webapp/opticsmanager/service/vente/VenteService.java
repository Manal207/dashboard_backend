package tech.webapp.opticsmanager.service.vente;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.webapp.opticsmanager.exception.*;
import tech.webapp.opticsmanager.model.User;
import tech.webapp.opticsmanager.model.organization.client.*;
import tech.webapp.opticsmanager.model.products.*;
import tech.webapp.opticsmanager.model.vente.*;
import tech.webapp.opticsmanager.repo.organization.client.*;
import tech.webapp.opticsmanager.repo.products.*;
import tech.webapp.opticsmanager.repo.vente.*;
import tech.webapp.opticsmanager.service.products.*;

import tech.webapp.opticsmanager.dto.vente.*;
import tech.webapp.opticsmanager.mapper.VenteMapper;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class VenteService {

    private final VenteRepo venteRepo;
    private final LigneVenteRepo ligneVenteRepo;
    private final MagasinRepo magazinRepo;
    private final ParticulierRepo particulierRepo;
    private final MontureService montureService;
    private final VerreService verreService;
    private final LentilleService lentilleService;
    private final AccessoireService accessoireService;

    private static final BigDecimal TVA_RATE = new BigDecimal("0.20"); // 20% TVA

    @Autowired
    public VenteService(VenteRepo venteRepo, LigneVenteRepo ligneVenteRepo,
                        MagasinRepo magazinRepo, ParticulierRepo particulierRepo,
                        MontureService montureService, VerreService verreService,
                        LentilleService lentilleService, AccessoireService accessoireService, VenteMapper venteMapper) {
        this.venteRepo = venteRepo;
        this.ligneVenteRepo = ligneVenteRepo;
        this.magazinRepo = magazinRepo;
        this.particulierRepo = particulierRepo;
        this.montureService = montureService;
        this.verreService = verreService;
        this.lentilleService = lentilleService;
        this.accessoireService = accessoireService;
        this.venteMapper = venteMapper;
    }

    public Vente addVente(Vente vente) {
        vente.setNumeroVente(generateNumeroVente());
        vente.setDateVente(LocalDateTime.now());
        if (vente.getStatut() == null) {
            vente.setStatut(Vente.StatutVente.BROUILLON);
        }
        calculateTotals(vente);
        return venteRepo.save(vente);
    }

    public Vente updateVente(Vente vente) {
        Vente existingVente = findVenteById(vente.getId());
        calculateTotals(vente);
        return venteRepo.save(vente);
    }

    public void deleteVente(Long id) {
        venteRepo.deleteVenteById(id);
    }

    public List<Vente> findAllVentes() {
        return venteRepo.findAll();
    }

    public Vente findVenteById(Long id) {
        return venteRepo.findVenteById(id).orElseThrow(()-> new RuntimeException("vente not found"));

    }

    public Vente findVenteWithLignes(Long id) {
        return venteRepo.findByIdWithLignes(id)
                .orElseThrow(() -> new RuntimeException("vente not found"));
    }

    @Transactional
    public Vente addLigneVente(Long venteId, LigneVente ligneVente) {
        Vente vente = findVenteById(venteId);

        // Set prix unitaire selon le type de produit
        setPrixUnitaire(ligneVente);

        // Calculer le prix total de la ligne
        calculateLignePrixTotal(ligneVente);

        // Ajouter la ligne à la vente
        ligneVente.setVente(vente);
        vente.getLignesVente().add(ligneVente);

        // Recalculer les totaux
        calculateTotals(vente);

        return venteRepo.save(vente);
    }

    @Transactional
    public Vente removeLigneVente(Long venteId, Long ligneId) {
        Vente vente = findVenteById(venteId);

        LigneVente ligne = ligneVenteRepo.findLigneVenteById(ligneId)
                .orElseThrow(() -> new RuntimeException("LigneVente not found"));

        vente.getLignesVente().remove(ligne);
        ligneVenteRepo.delete(ligne);

        calculateTotals(vente);

        return venteRepo.save(vente);
    }

    public Vente updateStatut(Long id, Vente.StatutVente nouveauStatut) {
        Vente vente = findVenteById(id);
        vente.setStatut(nouveauStatut);

        // Si la vente est confirmée, on peut déclencher d'autres actions
        if (nouveauStatut == Vente.StatutVente.CONFIRMEE) {
            // Par exemple, réduire le stock
            updateStock(vente);
        }

        return venteRepo.save(vente);
    }

    // Méthodes privées utilitaires

    private void setPrixUnitaire(LigneVente ligneVente) {
        BigDecimal prixUnitaire = BigDecimal.ZERO;

        switch (ligneVente.getTypeProduit()) {
            case MONTURE:
                if (ligneVente.getMonture() != null) {
                    prixUnitaire = ligneVente.getMonture().getPrixVente();
                }
                break;
            case VERRE:
                if (ligneVente.getVerre() != null) {
                    prixUnitaire = ligneVente.getVerre().getPrixVente();
                }
                break;
            case LENTILLE:
                if (ligneVente.getLentille() != null) {
                    prixUnitaire = ligneVente.getLentille().getPrixVente();
                }
                break;
            case ACCESSOIRE:
                if (ligneVente.getAccessoire() != null) {
                    prixUnitaire = ligneVente.getAccessoire().getPrixVente();
                }
                break;
        }

        ligneVente.setPrixUnitaire(prixUnitaire);
    }

    private void calculateLignePrixTotal(LigneVente ligne) {
        BigDecimal prixBase = ligne.getPrixUnitaire().multiply(BigDecimal.valueOf(ligne.getQuantite()));
        BigDecimal remise = ligne.getRemise() != null ? ligne.getRemise() : BigDecimal.ZERO;
        ligne.setPrixTotal(prixBase.subtract(remise));
    }

    private void calculateTotals(Vente vente) {
        BigDecimal montantTotal = vente.getLignesVente().stream()
                .map(LigneVente::getPrixTotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        vente.setMontantTotal(montantTotal);
        vente.setMontantTVA(montantTotal.multiply(TVA_RATE));
        vente.setMontantTTC(montantTotal.add(vente.getMontantTVA()));
    }

    private String generateNumeroVente() {
        int year = LocalDate.now().getYear();
        Long count = venteRepo.countVentesForYear(year) + 1;
        return String.format("V-%d-%04d", year, count);
    }

    private void updateStock(Vente vente) {
        // Logique pour mettre à jour le stock
        // À implémenter selon vos besoins
    }

    // Méthodes pour gérer les clients (Magasin ou Particulier)

    public Vente setClientMagasin(Long venteId, Long magasinId) {
        Vente vente = findVenteById(venteId);
        Magasin magasin = magazinRepo.findById(magasinId)
                .orElseThrow(() -> new RuntimeException("Magasin not found"));

        vente.setMagasinClient(magasin);
        vente.setParticulierClient(null);
        vente.setTypeClient(Vente.TypeClient.MAGASIN);

        return venteRepo.save(vente);
    }

    public Vente setClientParticulier(Long venteId, Long particulierId) {
        Vente vente = findVenteById(venteId);
        Particulier particulier = particulierRepo.findById(particulierId)
                .orElseThrow(() -> new RuntimeException("Particulier not found"));

        vente.setParticulierClient(particulier);
        vente.setMagasinClient(null);
        vente.setTypeClient(Vente.TypeClient.PARTICULIER);

        return venteRepo.save(vente);
    }

    // Ajouter dans le constructor
    private final VenteMapper venteMapper;

    // Méthodes avec DTOs
    public VenteDTO createVenteFromDTO(CreateVenteDTO createDTO) {
        Vente vente = new Vente();
        vente.setDateVente(LocalDateTime.now());
        vente.setNumeroVente(generateNumeroVente());
        vente.setStatut(Vente.StatutVente.BROUILLON);
        vente.setNotes(createDTO.getNotes());

        // Get current user from security context
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof User) {
            User currentUser = (User) authentication.getPrincipal();
            vente.setCreatedBy(currentUser);
        }

        // Set client selon le type
        if (createDTO.getTypeClient() == Vente.TypeClient.MAGASIN) {
            Magasin magasin = magazinRepo.findById(createDTO.getClientId())
                    .orElseThrow(() -> new RuntimeException("Magasin not found"));
            vente.setMagasinClient(magasin);
            vente.setTypeClient(Vente.TypeClient.MAGASIN);
        } else {
            Particulier particulier = particulierRepo.findById(createDTO.getClientId())
                    .orElseThrow(() -> new RuntimeException("Particulier not found"));
            vente.setParticulierClient(particulier);
            vente.setTypeClient(Vente.TypeClient.PARTICULIER);
        }

        // Sauvegarder d'abord la vente
        vente = venteRepo.save(vente);

        // Ajouter les lignes de vente si présentes
        if (createDTO.getLignesVente() != null && !createDTO.getLignesVente().isEmpty()) {
            for (CreateLigneVenteDTO ligneDTO : createDTO.getLignesVente()) {
                addLigneVenteFromDTO(vente.getId(), ligneDTO);
            }
        }

        // Recharger la vente avec toutes les lignes
        vente = findVenteWithLignes(vente.getId());

        return venteMapper.toDTO(vente);
    }

    public VenteDTO addLigneVenteFromDTO(Long venteId, CreateLigneVenteDTO ligneDTO) {
        Vente vente = findVenteById(venteId);

        LigneVente ligne = new LigneVente();
        ligne.setTypeProduit(ligneDTO.getTypeProduit());
        ligne.setQuantite(ligneDTO.getQuantite());
        ligne.setRemise(ligneDTO.getRemise() != null ? ligneDTO.getRemise() : BigDecimal.ZERO);

        // Set le produit selon le type
        switch (ligneDTO.getTypeProduit()) {
            case MONTURE:
                Monture monture = montureService.findMontureById(ligneDTO.getProduitId());
                ligne.setMonture(monture);
                ligne.setPrixUnitaire(monture.getPrixVente());
                break;
            case VERRE:
                Verre verre = verreService.findVerreById(ligneDTO.getProduitId());
                ligne.setVerre(verre);
                ligne.setPrixUnitaire(verre.getPrixVente());

                // Ajouter la prescription si fournie
                if (ligneDTO.getPrescription() != null) {
                    PrescriptionOptique prescription = venteMapper.toEntity(ligneDTO.getPrescription());
                    ligne.setPrescription(prescription);
                }
                break;
            case LENTILLE:
                Lentille lentille = lentilleService.findLentilleById(ligneDTO.getProduitId());
                ligne.setLentille(lentille);
                ligne.setPrixUnitaire(lentille.getPrixVente());
                break;
            case ACCESSOIRE:
                Accessoire accessoire = accessoireService.findAccessoireById(ligneDTO.getProduitId());
                ligne.setAccessoire(accessoire);
                ligne.setPrixUnitaire(accessoire.getPrixVente());
                break;
        }

        // Calculer le prix total
        calculateLignePrixTotal(ligne);

        // Ajouter à la vente
        ligne.setVente(vente);
        vente.getLignesVente().add(ligne);

        // Recalculer les totaux
        calculateTotals(vente);

        vente = venteRepo.save(vente);

        return venteMapper.toDTO(vente);
    }

    public List<VenteDTO> findAllVentesDTO() {
        return findAllVentes().stream()
                .map(venteMapper::toDTO)
                .collect(Collectors.toList());
    }

    public VenteDTO findVenteByIdDTO(Long id) {
        return venteMapper.toDTO(findVenteById(id));
    }
}