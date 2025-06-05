package tech.webapp.opticsmanager.repo.products;

import org.springframework.data.jpa.repository.JpaRepository;
import tech.webapp.opticsmanager.model.products.Lentille;

import java.util.Optional;

public interface LentilleRepo extends JpaRepository<Lentille, Long> {
    void deleteLentilleById(Long id);
    Optional<Lentille> findLentilleById(Long id);

    void deleteAllByIdInBatch(Iterable<Long> ids);

}
