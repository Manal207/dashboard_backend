package tech.webapp.opticsmanager.repo.sales;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tech.webapp.opticsmanager.model.sales.FicheVente;
import tech.webapp.opticsmanager.model.sales.Vente;

import java.util.List;

@Repository
public interface FicheVenteRepository extends JpaRepository<FicheVente, Long> {

    List<FicheVente> findByVenteOrderByOrdre(Vente vente);

    void deleteByVente(Vente vente);

    Long countByVente(Vente vente);
}