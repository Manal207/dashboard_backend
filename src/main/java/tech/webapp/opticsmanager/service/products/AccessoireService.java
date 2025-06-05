package tech.webapp.opticsmanager.service.products;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.webapp.opticsmanager.model.products.Accessoire;
import tech.webapp.opticsmanager.repo.products.AccessoireRepo;

import java.util.List;

@Service
public class AccessoireService {
    private final AccessoireRepo accessoireRepo;

    @Autowired
    public AccessoireService(AccessoireRepo accessoireRepo) {
        this.accessoireRepo = accessoireRepo;
    }

    public Accessoire addAccessoire(Accessoire accessoire) {
        return accessoireRepo.save(accessoire);
    }

    public Accessoire updateAccessoire(Accessoire accessoire) {
        return accessoireRepo.save(accessoire);
    }

    @Transactional
    public void deleteAccessoire(Long id) {
        accessoireRepo.deleteAccessoireById(id);
    }

    public List<Accessoire> findAllAccessoire() {
        return accessoireRepo.findAll();
    }

    public Accessoire findAccessoireById(Long id) {
        return accessoireRepo.findAccessoireById(id).orElseThrow(() -> new RuntimeException("Accessoire not found"));
    }

    public void deleteAccessoires(List<Long> ids) {
        accessoireRepo.deleteAllByIdInBatch(ids);
    }
}
