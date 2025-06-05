package tech.webapp.opticsmanager.service.products;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.webapp.opticsmanager.exception.MontureNotFoundException;
import tech.webapp.opticsmanager.model.products.Monture;
import tech.webapp.opticsmanager.repo.products.MontureRepo;

import java.util.List;

@Service
public class MontureService {
    private final MontureRepo montureRepo;

    @Autowired
    public MontureService(MontureRepo montureRepo) {
        this.montureRepo = montureRepo;
    }

    public Monture addMonture(Monture monture) {
        return montureRepo.save(monture);
    }

    public Monture updateMonture(Monture monture) {
        return montureRepo.save(monture);
    }

    @Transactional
    public void deleteMonture(Long id) {
        montureRepo.deleteMontureById(id);
    }

    public List<Monture> findAllMonture() {
        return montureRepo.findAll();
    }
    public Monture findMontureById(Long id) {
        return montureRepo.findMontureById(id)
                .orElseThrow(()-> new MontureNotFoundException("Monture by id not found"));
    }


    public void deleteMontures(List<Long> ids) {
        montureRepo.deleteAllByIdInBatch(ids);
    }



}
