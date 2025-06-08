package tech.webapp.opticsmanager.repo.sales;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tech.webapp.opticsmanager.model.sales.FicheAccessoire;
import tech.webapp.opticsmanager.model.sales.FicheVente;

import java.util.List;

@Repository
public interface FicheAccessoireRepository extends JpaRepository<FicheAccessoire, Long> {

    List<FicheAccessoire> findByFicheVenteOrderByOrdre(FicheVente ficheVente);

    void deleteByFicheVente(FicheVente ficheVente);

    Long countByFicheVente(FicheVente ficheVente);
}