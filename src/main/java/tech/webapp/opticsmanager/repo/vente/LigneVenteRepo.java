package tech.webapp.opticsmanager.repo.vente;

import org.springframework.data.jpa.repository.JpaRepository;
import tech.webapp.opticsmanager.model.vente.LigneVente;

import java.util.List;
import java.util.Optional;

public interface LigneVenteRepo extends JpaRepository<LigneVente, Long> {

    Optional<LigneVente> findLigneVenteById(Long id);

    List<LigneVente> findByVenteId(Long venteId);

    void deleteLigneVenteById(Long id);
}
