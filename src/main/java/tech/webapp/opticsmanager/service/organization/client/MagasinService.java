package tech.webapp.opticsmanager.service.organization.client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.webapp.opticsmanager.model.organization.client.Magasin;
import tech.webapp.opticsmanager.repo.organization.client.MagasinRepo;

import java.util.List;

@Service
public class MagasinService {
    private final MagasinRepo magasinRepo;

    @Autowired
    public MagasinService(MagasinRepo magasinRepo) {
        this.magasinRepo = magasinRepo;
    }

    public Magasin addMagasin(Magasin magasin) {
        return magasinRepo.save(magasin);
    }

    public Magasin updateMagasin(Magasin magasin) {
        return magasinRepo.save(magasin);
    }

    @Transactional
    public void deleteMagasin(Long id) {
        magasinRepo.deleteMagasinById(id);
    }

    public List<Magasin> findAllMagasin() {
        return magasinRepo.findAll();
    }

    public Magasin findMagasinById(Long id) {
        return magasinRepo.findMagasinById(id).orElseThrow(() -> new RuntimeException("Magasin not found"));
    }

    public void deleteMagasins(List<Long> ids) {
        magasinRepo.deleteAllByIdInBatch(ids);
    }
}
