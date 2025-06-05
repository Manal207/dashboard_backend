package tech.webapp.opticsmanager.repo.products;

import org.springframework.data.jpa.repository.JpaRepository;
import tech.webapp.opticsmanager.model.products.Monture;

import java.util.List;
import java.util.Optional;

public interface MontureRepo extends JpaRepository<Monture, Long> {
    void deleteMontureById(Long id);
    Optional <Monture> findMontureById(Long id);

    void deleteAllByIdInBatch(Iterable<Long> ids);


}
