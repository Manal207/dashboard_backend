package tech.webapp.opticsmanager.service.products;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.webapp.opticsmanager.model.products.Verre;
import tech.webapp.opticsmanager.repo.products.VerreRepo;

import java.util.List;

@Service
public class VerreService {
    private final VerreRepo verreRepo;

    @Autowired
    public VerreService(VerreRepo verreRepo) {
        this.verreRepo = verreRepo;
    }

    public Verre addVerre(Verre verre) {
        return verreRepo.save(verre);
    }

    public Verre updateVerre(Verre verre) {
        return verreRepo.save(verre);
    }
    @Transactional
    public void deleteVerre(Long id) {
        verreRepo.deleteVerreById(id);
    }

    public List<Verre> findAllVerre() {
        return verreRepo.findAll();
    }

    public Verre findVerreById(Long id) {
        return verreRepo.findVerreById(id).orElseThrow(() -> new RuntimeException("Verre not found"));
    }

    public void deleteVerres(List<Long> ids) {
        verreRepo.deleteAllByIdInBatch(ids);
    }
}
