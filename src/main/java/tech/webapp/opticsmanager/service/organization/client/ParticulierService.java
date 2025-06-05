package tech.webapp.opticsmanager.service.organization.client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.webapp.opticsmanager.model.organization.client.Particulier;
import tech.webapp.opticsmanager.repo.organization.client.ParticulierRepo;

import java.util.List;

@Service
public class ParticulierService {
    private final ParticulierRepo particulierRepo;

    @Autowired
    public ParticulierService(ParticulierRepo particulierRepo) {
        this.particulierRepo = particulierRepo;
    }

    public Particulier addParticulier(Particulier particulier) {
        return particulierRepo.save(particulier);
    }

    public Particulier updateParticulier(Particulier particulier) {
        return particulierRepo.save(particulier);
    }

    @Transactional
    public void deleteParticulier(Long id) {
        particulierRepo.deleteParticulierById(id);
    }

    public List<Particulier> findAllParticulier() {
        return particulierRepo.findAll();
    }

    public Particulier findParticulierById(Long id) {
        return particulierRepo.findParticulierById(id)
                .orElseThrow(() -> new RuntimeException("Particulier not found"));
    }

    public void deleteParticuliers(List<Long> ids) {
        particulierRepo.deleteAllByIdInBatch(ids);
    }
}
