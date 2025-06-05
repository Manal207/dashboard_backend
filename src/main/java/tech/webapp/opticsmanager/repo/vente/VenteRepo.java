package tech.webapp.opticsmanager.repo.vente;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import tech.webapp.opticsmanager.model.vente.Vente;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface VenteRepo extends JpaRepository<Vente, Long> {

    Optional<Vente> findVenteById(Long id);

    Optional<Vente> findByNumeroVente(String numeroVente);

    void deleteVenteById(Long id);

    Page<Vente> findByClientId(Long clientId, Pageable pageable);

    Page<Vente> findByStatut(Vente.StatutVente statut, Pageable pageable);

    @Query("SELECT v FROM Vente v WHERE v.dateVente BETWEEN :debut AND :fin")
    List<Vente> findVentesBetweenDates(@Param("debut") LocalDateTime debut,
                                       @Param("fin") LocalDateTime fin);

    @Query("SELECT COUNT(v) FROM Vente v WHERE YEAR(v.dateVente) = :year")
    Long countVentesForYear(@Param("year") int year);

    @Query("SELECT v FROM Vente v JOIN FETCH v.lignesVente WHERE v.id = :id")
    Optional<Vente> findByIdWithLignes(@Param("id") Long id);
}