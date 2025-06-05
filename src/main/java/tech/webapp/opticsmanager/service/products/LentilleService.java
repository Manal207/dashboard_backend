package tech.webapp.opticsmanager.service.products;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.webapp.opticsmanager.model.products.Lentille;
import tech.webapp.opticsmanager.repo.products.LentilleRepo;

import java.util.List;

@Service
public class LentilleService {

    private final LentilleRepo lentilleRepo;

    @Autowired
    public LentilleService(LentilleRepo lentilleRepo) {
        this.lentilleRepo = lentilleRepo;
    }

    public Lentille addLentille(Lentille lentille) {
        return lentilleRepo.save(lentille);
    }
    public Lentille updateLentille(Lentille lentille) {
        return lentilleRepo.save(lentille);
    }

    @Transactional
    public void deleteLentille(Long id) {
        lentilleRepo.deleteLentilleById(id);
    }
    public List<Lentille> findAllLentille(){
        return lentilleRepo.findAll();
    }
    public Lentille findLentilleById(Long id) {
        return lentilleRepo.findById(id).orElseThrow(()-> new RuntimeException("Lentille not found"));
    }


    public void deleteLentilles(List<Long> ids) {
        lentilleRepo.deleteAllByIdInBatch(ids);
    }


}
