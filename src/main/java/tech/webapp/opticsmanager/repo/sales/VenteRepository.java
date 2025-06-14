package tech.webapp.opticsmanager.repo.sales;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import tech.webapp.opticsmanager.model.sales.Vente;
import tech.webapp.opticsmanager.model.User;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface VenteRepository extends JpaRepository<Vente, Long> {

    Optional<Vente> findByNumeroVente(String numeroVente);

    List<Vente> findByCreateurOrderByDateCreationDesc(User createur);

    List<Vente> findByStatutOrderByDateCreationDesc(Vente.StatutVente statut);

    List<Vente> findByTypeClientOrderByDateCreationDesc(Vente.TypeClient typeClient);

    @Query("SELECT v FROM Vente v WHERE v.dateCreation BETWEEN :debut AND :fin ORDER BY v.dateCreation DESC")
    List<Vente> findVentesByPeriode(@Param("debut") LocalDateTime debut, @Param("fin") LocalDateTime fin);

    @Query("SELECT COUNT(v) FROM Vente v WHERE v.numeroVente LIKE :pattern")
    Long countByNumeroVentePattern(@Param("pattern") String pattern);

    @Query("SELECT v FROM Vente v WHERE " +
            "(:numeroVente IS NULL OR v.numeroVente LIKE %:numeroVente%) AND " +
            "(:createurId IS NULL OR v.createur.id = :createurId) AND " +
            "(:typeClient IS NULL OR v.typeClient = :typeClient) AND " +
            "(:statut IS NULL OR v.statut = :statut)")
    List<Vente> searchVentes(@Param("numeroVente") String numeroVente,
                             @Param("createurId") Long createurId,
                             @Param("typeClient") Vente.TypeClient typeClient,
                             @Param("statut") Vente.StatutVente statut);

    // Add this method to your VenteRepository.java:
    @Query("SELECT v.numeroVente FROM Vente v " +
            "WHERE v.numeroVente LIKE :pattern " +
            "ORDER BY v.numeroVente DESC " +
            "LIMIT 1")
    Optional<String> findLastNumeroByYearPattern(@Param("pattern") String pattern);

}