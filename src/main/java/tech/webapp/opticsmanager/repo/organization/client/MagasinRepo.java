package tech.webapp.opticsmanager.repo.organization.client;

import org.springframework.data.jpa.repository.JpaRepository;
import tech.webapp.opticsmanager.model.organization.client.Magasin;

import java.util.Optional;

public interface MagasinRepo extends JpaRepository<Magasin, Long> {
    void deleteMagasinById(Long id);
    Optional<Magasin> findMagasinById(Long id);
    void deleteAllByIdInBatch(Iterable<Long> ids);

}
