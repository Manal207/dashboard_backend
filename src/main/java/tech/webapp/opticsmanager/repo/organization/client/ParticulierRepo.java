package tech.webapp.opticsmanager.repo.organization.client;

import org.springframework.data.jpa.repository.JpaRepository;
import tech.webapp.opticsmanager.model.organization.client.Particulier;

import java.util.Optional;

public interface ParticulierRepo extends JpaRepository<Particulier, Long> {
    void deleteParticulierById(Long id);
    Optional<Particulier> findParticulierById(Long id);
    void deleteAllByIdInBatch(Iterable<Long> ids);
}
