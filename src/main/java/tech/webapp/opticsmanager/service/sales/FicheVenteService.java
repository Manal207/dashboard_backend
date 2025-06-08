package tech.webapp.opticsmanager.service.sales;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.webapp.opticsmanager.dto.sales.*;
import tech.webapp.opticsmanager.exception.ResourceNotFoundException;
import tech.webapp.opticsmanager.model.sales.*;
import tech.webapp.opticsmanager.model.products.Monture;
import tech.webapp.opticsmanager.model.products.Verre;
import tech.webapp.opticsmanager.model.products.Accessoire;
import tech.webapp.opticsmanager.repo.sales.FicheVenteRepository;
import tech.webapp.opticsmanager.repo.sales.FicheAccessoireRepository; // ← CHANGER ICI

//import tech.webapp.opticsmanager.repo.sales.FicheAccessoireRepository;
import tech.webapp.opticsmanager.service.products.MontureService;
import tech.webapp.opticsmanager.service.products.VerreService;
import tech.webapp.opticsmanager.service.products.AccessoireService;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class FicheVenteService {

    @Autowired
    private FicheVenteRepository ficheVenteRepository;

    @Autowired
    private FicheAccessoireRepository ficheAccessoireRepository;

    @Autowired
    private MontureService montureService;

    @Autowired
    private VerreService verreService;

    @Autowired
    private AccessoireService accessoireService;

    public List<FicheVenteDTO> createFiches(Vente vente, List<CreateFicheRequest> requests) {
        List<FicheVenteDTO> fichesDTO = new ArrayList<>();

        for (CreateFicheRequest request : requests) {
            FicheVenteDTO ficheDTO = createFiche(vente, request);
            fichesDTO.add(ficheDTO);
        }

        return fichesDTO;
    }

    public FicheVenteDTO createFiche(Vente vente, CreateFicheRequest request) {
        FicheVente fiche = new FicheVente();
        fiche.setVente(vente);
        fiche.setOrdre(request.getOrdre());

        // Récupérer et associer la monture
        Monture monture = montureService.findMontureById(request.getMontureId());
        fiche.setMonture(monture);
        fiche.setPrixMonture(request.getPrixMonture() != null ?
                request.getPrixMonture() : monture.getPrixVente());

        // Récupérer et associer les verres
        Verre verreOD = verreService.findVerreById(request.getVerreODId());
        fiche.setVerreOD(verreOD);
        fiche.setPrixVerreOD(request.getPrixVerreOD() != null ?
                request.getPrixVerreOD() : verreOD.getPrixVente());

        Verre verreOG = verreService.findVerreById(request.getVerreOGId());
        fiche.setVerreOG(verreOG);
        fiche.setPrixVerreOG(request.getPrixVerreOG() != null ?
                request.getPrixVerreOG() : verreOG.getPrixVente());

        // Associer les mesures
        fiche.setMesuresOD(convertToMesuresOeil(request.getMesuresOD()));
        fiche.setMesuresOG(convertToMesuresOeil(request.getMesuresOG()));

        // Sauvegarder la fiche d'abord
        fiche = ficheVenteRepository.save(fiche);

        // Créer les accessoires
        if (request.getAccessoires() != null && !request.getAccessoires().isEmpty()) {
            createAccessoires(fiche, request.getAccessoires());
        }

        // Calculer le total de la fiche
        calculerTotalFiche(fiche);

        return convertToDTO(fiche);
    }

    public void updateFiches(List<UpdateFicheRequest> requests) {
        for (UpdateFicheRequest request : requests) {
            updateFiche(request);
        }
    }

    public FicheVenteDTO updateFiche(UpdateFicheRequest request) {
        FicheVente fiche = ficheVenteRepository.findById(request.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Fiche not found with id: " + request.getId()));

        // Mise à jour des propriétés
        if (request.getOrdre() != null) {
            fiche.setOrdre(request.getOrdre());
        }

        if (request.getMontureId() != null) {
            Monture monture = montureService.findMontureById(request.getMontureId());
            fiche.setMonture(monture);
            fiche.setPrixMonture(request.getPrixMonture() != null ?
                    request.getPrixMonture() : monture.getPrixVente());
        } else if (request.getPrixMonture() != null) {
            fiche.setPrixMonture(request.getPrixMonture());
        }

        if (request.getVerreODId() != null) {
            Verre verreOD = verreService.findVerreById(request.getVerreODId());
            fiche.setVerreOD(verreOD);
            fiche.setPrixVerreOD(request.getPrixVerreOD() != null ?
                    request.getPrixVerreOD() : verreOD.getPrixVente());
        } else if (request.getPrixVerreOD() != null) {
            fiche.setPrixVerreOD(request.getPrixVerreOD());
        }

        if (request.getVerreOGId() != null) {
            Verre verreOG = verreService.findVerreById(request.getVerreOGId());
            fiche.setVerreOG(verreOG);
            fiche.setPrixVerreOG(request.getPrixVerreOG() != null ?
                    request.getPrixVerreOG() : verreOG.getPrixVente());
        } else if (request.getPrixVerreOG() != null) {
            fiche.setPrixVerreOG(request.getPrixVerreOG());
        }

        if (request.getMesuresOD() != null) {
            fiche.setMesuresOD(convertToMesuresOeil(request.getMesuresOD()));
        }

        if (request.getMesuresOG() != null) {
            fiche.setMesuresOG(convertToMesuresOeil(request.getMesuresOG()));
        }

        // Gestion des accessoires
        if (request.getAccessoiresASupprimer() != null && !request.getAccessoiresASupprimer().isEmpty()) {
            deleteAccessoires(request.getAccessoiresASupprimer());
        }

        if (request.getAccessoires() != null && !request.getAccessoires().isEmpty()) {
            updateAccessoires(fiche, request.getAccessoires());
        }

        fiche = ficheVenteRepository.save(fiche);
        calculerTotalFiche(fiche);

        return convertToDTO(fiche);
    }

    public void deleteFiches(List<Long> ficheIds) {
        for (Long ficheId : ficheIds) {
            deleteFiche(ficheId);
        }
    }

    public void deleteFiche(Long ficheId) {
        FicheVente fiche = ficheVenteRepository.findById(ficheId)
                .orElseThrow(() -> new ResourceNotFoundException("Fiche not found with id: " + ficheId));
        ficheVenteRepository.delete(fiche);
    }

    private void createAccessoires(FicheVente fiche, List<CreateAccessoireRequest> requests) {
        for (int i = 0; i < requests.size(); i++) {
            CreateAccessoireRequest request = requests.get(i);

            FicheAccessoire ficheAccessoire = new FicheAccessoire();
            ficheAccessoire.setFicheVente(fiche);

            Accessoire accessoire = accessoireService.findAccessoireById(request.getAccessoireId());
            ficheAccessoire.setAccessoire(accessoire);
            ficheAccessoire.setPrixAccessoire(request.getPrixAccessoire() != null ?
                    request.getPrixAccessoire() : accessoire.getPrixVente());
            ficheAccessoire.setOrdre(request.getOrdre() != null ? request.getOrdre() : i + 1);

            ficheAccessoireRepository.save(ficheAccessoire);
        }
    }

    private void updateAccessoires(FicheVente fiche, List<UpdateAccessoireRequest> requests) {
        for (UpdateAccessoireRequest request : requests) {
            if (request.getId() != null) {
                // Mise à jour d'un accessoire existant
                FicheAccessoire ficheAccessoire = ficheAccessoireRepository.findById(request.getId())
                        .orElseThrow(() -> new ResourceNotFoundException("Accessoire not found with id: " + request.getId()));

                if (request.getAccessoireId() != null) {
                    Accessoire accessoire = accessoireService.findAccessoireById(request.getAccessoireId());
                    ficheAccessoire.setAccessoire(accessoire);
                    ficheAccessoire.setPrixAccessoire(request.getPrixAccessoire() != null ?
                            request.getPrixAccessoire() : accessoire.getPrixVente());
                } else if (request.getPrixAccessoire() != null) {
                    ficheAccessoire.setPrixAccessoire(request.getPrixAccessoire());
                }

                if (request.getOrdre() != null) {
                    ficheAccessoire.setOrdre(request.getOrdre());
                }

                ficheAccessoireRepository.save(ficheAccessoire);
            } else {
                // Création d'un nouvel accessoire
                FicheAccessoire ficheAccessoire = new FicheAccessoire();
                ficheAccessoire.setFicheVente(fiche);

                Accessoire accessoire = accessoireService.findAccessoireById(request.getAccessoireId());
                ficheAccessoire.setAccessoire(accessoire);
                ficheAccessoire.setPrixAccessoire(request.getPrixAccessoire() != null ?
                        request.getPrixAccessoire() : accessoire.getPrixVente());
                ficheAccessoire.setOrdre(request.getOrdre() != null ? request.getOrdre() : 1);

                ficheAccessoireRepository.save(ficheAccessoire);
            }
        }
    }

    private void deleteAccessoires(List<Long> accessoireIds) {
        for (Long accessoireId : accessoireIds) {
            FicheAccessoire ficheAccessoire = ficheAccessoireRepository.findById(accessoireId)
                    .orElseThrow(() -> new ResourceNotFoundException("Accessoire not found with id: " + accessoireId));
            ficheAccessoireRepository.delete(ficheAccessoire);
        }
    }

    private void calculerTotalFiche(FicheVente fiche) {
        BigDecimal total = BigDecimal.ZERO;

        if (fiche.getPrixMonture() != null) {
            total = total.add(fiche.getPrixMonture());
        }

        if (fiche.getPrixVerreOD() != null) {
            total = total.add(fiche.getPrixVerreOD());
        }

        if (fiche.getPrixVerreOG() != null) {
            total = total.add(fiche.getPrixVerreOG());
        }

        // CORRECTION: Recharger les accessoires avant le calcul
        if (fiche.getId() != null) {
            fiche = ficheVenteRepository.findById(fiche.getId()).orElse(fiche);
        }

        // Ajouter le prix des accessoires
        if (fiche.getAccessoires() != null && !fiche.getAccessoires().isEmpty()) {
            BigDecimal totalAccessoires = fiche.getAccessoires().stream()
                    .map(acc -> acc.getPrixAccessoire() != null ? acc.getPrixAccessoire() : BigDecimal.ZERO)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);
            total = total.add(totalAccessoires);

            System.out.println("DEBUG - Accessoires: " + totalAccessoires + ", Total avec accessoires: " + total);
        }

        fiche.setTotalFiche(total);
        ficheVenteRepository.save(fiche);
    }

//    private void calculerTotalFiche(FicheVente fiche) {
//        BigDecimal total = BigDecimal.ZERO;
//
//        if (fiche.getPrixMonture() != null) {
//            total = total.add(fiche.getPrixMonture());
//        }
//
//        if (fiche.getPrixVerreOD() != null) {
//            total = total.add(fiche.getPrixVerreOD());
//        }
//
//        if (fiche.getPrixVerreOG() != null) {
//            total = total.add(fiche.getPrixVerreOG());
//        }
//
//        // Ajouter le prix des accessoires
//        if (fiche.getAccessoires() != null) {
//            BigDecimal totalAccessoires = fiche.getAccessoires().stream()
//                    .map(acc -> acc.getPrixAccessoire() != null ? acc.getPrixAccessoire() : BigDecimal.ZERO)
//                    .reduce(BigDecimal.ZERO, BigDecimal::add);
//            total = total.add(totalAccessoires);
//        }
//
//        fiche.setTotalFiche(total);
//        ficheVenteRepository.save(fiche);
//    }

    private MesuresOeil convertToMesuresOeil(MesuresOeilDTO dto) {
        if (dto == null) return null;
        return new MesuresOeil(dto.getSph(), dto.getCyl(), dto.getAxe());
    }

    private MesuresOeilDTO convertToMesuresOeilDTO(MesuresOeil mesures) {
        if (mesures == null) return null;
        return new MesuresOeilDTO(mesures.getSph(), mesures.getCyl(), mesures.getAxe());
    }

    public FicheVenteDTO convertToDTO(FicheVente fiche) {
        FicheVenteDTO dto = new FicheVenteDTO();
        dto.setId(fiche.getId());
        dto.setOrdre(fiche.getOrdre());

        // Monture
        if (fiche.getMonture() != null) {
            dto.setMontureId(fiche.getMonture().getId());
            dto.setMontureNom(fiche.getMonture().getMarque() + " " + fiche.getMonture().getReference());
        }
        dto.setPrixMonture(fiche.getPrixMonture());

        // Verres
        if (fiche.getVerreOD() != null) {
            dto.setVerreODId(fiche.getVerreOD().getId());
            dto.setVerreODNom(fiche.getVerreOD().getNom());
        }
        dto.setPrixVerreOD(fiche.getPrixVerreOD());

        if (fiche.getVerreOG() != null) {
            dto.setVerreOGId(fiche.getVerreOG().getId());
            dto.setVerreOGNom(fiche.getVerreOG().getNom());
        }
        dto.setPrixVerreOG(fiche.getPrixVerreOG());

        // Mesures
        dto.setMesuresOD(convertToMesuresOeilDTO(fiche.getMesuresOD()));
        dto.setMesuresOG(convertToMesuresOeilDTO(fiche.getMesuresOG()));

        // Accessoires
        if (fiche.getAccessoires() != null) {
            dto.setAccessoires(fiche.getAccessoires().stream()
                    .map(this::convertAccessoireToDTO)
                    .collect(Collectors.toList()));
        }

        dto.setTotalFiche(fiche.getTotalFiche());

        return dto;
    }

    private FicheAccessoireDTO convertAccessoireToDTO(FicheAccessoire ficheAccessoire) {
        FicheAccessoireDTO dto = new FicheAccessoireDTO();
        dto.setId(ficheAccessoire.getId());

        if (ficheAccessoire.getAccessoire() != null) {
            dto.setAccessoireId(ficheAccessoire.getAccessoire().getId());
            dto.setAccessoireNom(ficheAccessoire.getAccessoire().getNom());
            dto.setAccessoireReference(ficheAccessoire.getAccessoire().getReference());
        }

        dto.setPrixAccessoire(ficheAccessoire.getPrixAccessoire());
        dto.setOrdre(ficheAccessoire.getOrdre());

        return dto;
    }
}