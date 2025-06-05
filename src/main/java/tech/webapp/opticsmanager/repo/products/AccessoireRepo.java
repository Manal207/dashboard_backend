package tech.webapp.opticsmanager.repo.products;

import org.springframework.data.jpa.repository.JpaRepository;
import tech.webapp.opticsmanager.model.products.Accessoire;

import java.util.Optional;

public interface AccessoireRepo extends JpaRepository<Accessoire, Long> {
    void deleteAccessoireById(long id);
    Optional<Accessoire> findAccessoireById(long id);

    void deleteAllByIdInBatch(Iterable<Long> ids);
}
