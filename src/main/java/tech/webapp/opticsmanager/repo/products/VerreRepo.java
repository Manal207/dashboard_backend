package tech.webapp.opticsmanager.repo.products;

import org.springframework.data.jpa.repository.JpaRepository;
import tech.webapp.opticsmanager.model.products.Verre;

import java.util.Optional;

public interface VerreRepo extends JpaRepository<Verre, Long> {
    void deleteVerreById(long id);
    Optional<Verre> findVerreById(long id);

    void deleteAllByIdInBatch(Iterable<Long> ids);
}
