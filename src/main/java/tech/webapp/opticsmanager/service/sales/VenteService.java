package tech.webapp.opticsmanager.service.sales;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.webapp.opticsmanager.dto.sales.*;
import tech.webapp.opticsmanager.exception.ResourceNotFoundException;
import tech.webapp.opticsmanager.model.sales.FicheVente;
import tech.webapp.opticsmanager.model.sales.Vente;
import tech.webapp.opticsmanager.model.User;
import tech.webapp.opticsmanager.model.organization.client.Magasin;
import tech.webapp.opticsmanager.model.organization.client.Particulier;
import tech.webapp.opticsmanager.repo.sales.FicheVenteRepository;
import tech.webapp.opticsmanager.repo.sales.VenteRepository;
import tech.webapp.opticsmanager.service.UserService;
import tech.webapp.opticsmanager.service.organization.client.MagasinService;
import tech.webapp.opticsmanager.service.organization.client.ParticulierService;
import tech.webapp.opticsmanager.service.sales.FicheVenteService;


import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class VenteService {

    @Autowired
    private VenteRepository venteRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private MagasinService magasinService;

    @Autowired
    private ParticulierService particulierService;

    @Autowired
    private FicheVenteService ficheVenteService;

    public VenteDTO createVente(CreateVenteRequest request, Long createurId) {
        User createur = userService.findUserById(createurId);

        Vente vente = new Vente();
        vente.setNumeroVente(generateNumeroVente());
        vente.setCreateur(createur);
        vente.setUserNom(createur.getName());
        vente.setTypeClient(request.getTypeClient());

        // Initialiser explicitement la liste des fiches
        vente.setFiches(new ArrayList<>());

        // Associer le client selon le type
        if (request.getTypeClient() == Vente.TypeClient.MAGASIN) {
            Magasin magasin = magasinService.findMagasinById(request.getClientId());
            vente.setMagasin(magasin);
        } else {
            Particulier particulier = particulierService.findParticulierById(request.getClientId());
            vente.setParticulier(particulier);
        }

        vente.setNotes(request.getNotes());
        vente.setStatut(Vente.StatutVente.BROUILLON);
        vente.setTotalHT(BigDecimal.ZERO);
        vente.setTotalTTC(BigDecimal.ZERO);

        // Sauvegarder d'abord la vente
        vente = venteRepository.save(vente);

        // Créer les fiches si fournies
        if (request.getFiches() != null && !request.getFiches().isEmpty()) {
            ficheVenteService.createFiches(vente, request.getFiches());
            // Recharger la vente avec les fiches
            vente = venteRepository.findById(vente.getId()).orElse(vente);
            recalculerTotaux(vente);
        }

        return convertToDTO(vente);
    }

//    public VenteDTO createVente(CreateVenteRequest request, Long createurId) {
//        User createur = userService.findUserById(createurId);
//
//        Vente vente = new Vente();
//        vente.setNumeroVente(generateNumeroVente());
//        vente.setCreateur(createur);
//        vente.setUserNom(createur.getName());
//        vente.setTypeClient(request.getTypeClient());
//
//        // Associer le client selon le type
//        if (request.getTypeClient() == Vente.TypeClient.MAGASIN) {
//            Magasin magasin = magasinService.findMagasinById(request.getClientId());
//            vente.setMagasin(magasin);
//        } else {
//            Particulier particulier = particulierService.findParticulierById(request.getClientId());
//            vente.setParticulier(particulier);
//        }
//
//        vente.setNotes(request.getNotes());
//        vente.setStatut(Vente.StatutVente.BROUILLON);
//        vente.setTotalHT(BigDecimal.ZERO);
//        vente.setTotalTTC(BigDecimal.ZERO);
//
//        vente = venteRepository.save(vente);
//
//        // Créer les fiches si fournies
//        if (request.getFiches() != null && !request.getFiches().isEmpty()) {
//            ficheVenteService.createFiches(vente, request.getFiches());
//            recalculerTotaux(vente);
//        }
//
//        return convertToDTO(vente);
//    }

    public List<VenteDTO> findAllVentes() {
        return venteRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<VenteDTO> findVentesByCreateur(Long createurId) {
        User createur = userService.findUserById(createurId);
        return venteRepository.findByCreateurOrderByDateCreationDesc(createur).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public VenteDTO findVenteById(Long id) {
        Vente vente = venteRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Vente not found with id: " + id));
        return convertToDTO(vente);
    }

    public VenteDTO updateVente(Long id, UpdateVenteRequest request) {
        Vente vente = venteRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Vente not found with id: " + id));

        // Mise à jour des propriétés
        if (request.getTypeClient() != null) {
            vente.setTypeClient(request.getTypeClient());
        }

        if (request.getClientId() != null) {
            if (vente.getTypeClient() == Vente.TypeClient.MAGASIN) {
                Magasin magasin = magasinService.findMagasinById(request.getClientId());
                vente.setMagasin(magasin);
                vente.setParticulier(null);
            } else {
                Particulier particulier = particulierService.findParticulierById(request.getClientId());
                vente.setParticulier(particulier);
                vente.setMagasin(null);
            }
        }

        if (request.getNotes() != null) {
            vente.setNotes(request.getNotes());
        }

        if (request.getStatut() != null) {
            vente.setStatut(request.getStatut());
        }

        // Gestion des fiches
        if (request.getFichesASupprimer() != null && !request.getFichesASupprimer().isEmpty()) {
            ficheVenteService.deleteFiches(request.getFichesASupprimer());
        }

        if (request.getFiches() != null && !request.getFiches().isEmpty()) {
            ficheVenteService.updateFiches(request.getFiches());
        }

        if (request.getNouvelleFiches() != null && !request.getNouvelleFiches().isEmpty()) {
            ficheVenteService.createFiches(vente, request.getNouvelleFiches());
        }

        vente = venteRepository.save(vente);
        recalculerTotaux(vente);

        return convertToDTO(vente);
    }

    public void deleteVente(Long id) {
        Vente vente = venteRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Vente not found with id: " + id));
        venteRepository.delete(vente);
    }

    public FicheVenteDTO addFiche(Long venteId, CreateFicheRequest request) {
        Vente vente = venteRepository.findById(venteId)
                .orElseThrow(() -> new ResourceNotFoundException("Vente not found with id: " + venteId));

        FicheVenteDTO fiche = ficheVenteService.createFiche(vente, request);
        recalculerTotaux(vente);

        return fiche;
    }

    private String generateNumeroVente() {
        String year = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy"));
        String pattern = "V-" + year + "-%";
        Long count = venteRepository.countByNumeroVentePattern(pattern);
        return String.format("V-%s-%04d", year, count + 1);
    }

//    private void recalculerTotaux(Vente vente) {
//        BigDecimal totalHT = vente.getFiches().stream()
//                .map(fiche -> fiche.getTotalFiche() != null ? fiche.getTotalFiche() : BigDecimal.ZERO)
//                .reduce(BigDecimal.ZERO, BigDecimal::add);
//
//        vente.setTotalHT(totalHT);
//        vente.setTotalTTC(totalHT); // TODO: Ajouter la TVA si nécessaire
//
//        venteRepository.save(vente);
//    }

    private void recalculerTotaux(Vente vente) {
        // Recharger la vente avec toutes ses relations
        vente = venteRepository.findById(vente.getId()).orElse(vente);

        if (vente.getFiches() == null) {
            vente.setFiches(new ArrayList<>());
        }

        BigDecimal totalHT = BigDecimal.ZERO;

        for (FicheVente fiche : vente.getFiches()) {
            // Calculer directement le total de la fiche ici
            BigDecimal totalFiche = BigDecimal.ZERO;

            if (fiche.getPrixMonture() != null) {
                totalFiche = totalFiche.add(fiche.getPrixMonture());
            }

            if (fiche.getPrixVerreOD() != null) {
                totalFiche = totalFiche.add(fiche.getPrixVerreOD());
            }

            if (fiche.getPrixVerreOG() != null) {
                totalFiche = totalFiche.add(fiche.getPrixVerreOG());
            }

            // Ajouter les accessoires
            if (fiche.getAccessoires() != null) {
                fiche.getAccessoires().size(); // Force lazy loading
                BigDecimal totalAccessoires = fiche.getAccessoires().stream()
                        .map(acc -> acc.getPrixAccessoire() != null ? acc.getPrixAccessoire() : BigDecimal.ZERO)
                        .reduce(BigDecimal.ZERO, BigDecimal::add);
                totalFiche = totalFiche.add(totalAccessoires);
            }

            // Mettre à jour le total de la fiche si nécessaire
            if (fiche.getTotalFiche() == null || !fiche.getTotalFiche().equals(totalFiche)) {
                fiche.setTotalFiche(totalFiche);
            }

            totalHT = totalHT.add(totalFiche);
        }

        System.out.println("DEBUG - Total calculé: " + totalHT);

        vente.setTotalHT(totalHT);
        vente.setTotalTTC(totalHT);

        venteRepository.save(vente);
    }

//    private void recalculerTotaux(Vente vente) {
//        // S'assurer que la liste des fiches n'est pas null
//        if (vente.getFiches() == null) {
//            vente.setFiches(new ArrayList<>());
//        }
//
//        BigDecimal totalHT = vente.getFiches().stream()
//                .map(fiche -> fiche.getTotalFiche() != null ? fiche.getTotalFiche() : BigDecimal.ZERO)
//                .reduce(BigDecimal.ZERO, BigDecimal::add);
//
//        vente.setTotalHT(totalHT);
//        vente.setTotalTTC(totalHT); // TODO: Ajouter la TVA si nécessaire
//
//        venteRepository.save(vente);
//    }

    private VenteDTO convertToDTO(Vente vente) {
        VenteDTO dto = new VenteDTO();
        dto.setId(vente.getId());
        dto.setNumeroVente(vente.getNumeroVente());
        dto.setCreateurId(vente.getCreateur().getId());
        dto.setUserNom(vente.getUserNom());
        dto.setTypeClient(vente.getTypeClient());

        if (vente.getTypeClient() == Vente.TypeClient.MAGASIN && vente.getMagasin() != null) {
            dto.setClientId(vente.getMagasin().getId());
            dto.setClientNom(vente.getMagasin().getNom());
        } else if (vente.getTypeClient() == Vente.TypeClient.PARTICULIER && vente.getParticulier() != null) {
            dto.setClientId(vente.getParticulier().getId());
            dto.setClientNom(vente.getParticulier().getNom() + " " + vente.getParticulier().getPrenom());
        }

        dto.setDateCreation(vente.getDateCreation());
        dto.setDateModification(vente.getDateModification());
        dto.setStatut(vente.getStatut());
        dto.setTotalHT(vente.getTotalHT());
        dto.setTotalTTC(vente.getTotalTTC());
        dto.setNotes(vente.getNotes());

        if (vente.getFiches() != null) {
            dto.setFiches(vente.getFiches().stream()
                    .map(ficheVenteService::convertToDTO)
                    .collect(Collectors.toList()));
        }

        return dto;
    }
}