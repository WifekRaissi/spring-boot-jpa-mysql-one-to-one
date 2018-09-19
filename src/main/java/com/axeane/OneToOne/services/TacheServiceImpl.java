package com.axeane.OneToOne.services;

import com.axeane.OneToOne.model.Tache;
import com.axeane.OneToOne.repositories.SalariesRepository;
import com.axeane.OneToOne.repositories.TacheRepository;
import com.axeane.OneToOne.utils.ExceptionResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;

@Service
public class TacheServiceImpl implements TacheService {


    private final TacheRepository tacheRepository;
    private final SalariesRepository salariesRepository;

    public TacheServiceImpl(TacheRepository tacheRepository, SalariesRepository salariesRepository) {
        this.tacheRepository = tacheRepository;
        this.salariesRepository = salariesRepository;
    }

    @Override
    public void addTache(Long salarieId, Tache tache) {
        salariesRepository.findById(salarieId).map(salarie -> {
            tache.setSalarie(salarie);
            return tacheRepository.save(tache);
        });
    }

    @Override
    public Page<Tache> getAllTachesBySalarietId(Long salarieId,
                                                Pageable pageable) {
        return tacheRepository.findBySalarieId(salarieId, pageable);
    }

    @Override
    public void deleteTache(Long salarieId, Long tacheId) {
        if (!salariesRepository.existsById(salarieId)) {
            throw new ExceptionResponse.ResourceNotFoundException("tacheId" + tacheId + " not found");
        }
        tacheRepository.findById(tacheId).map(tache -> {
            tacheRepository.delete(tache);
            return tache;
        });
    }

    @Override
    public void updateTache(Long salarieId, Tache tacheRequest) {
        if (!salariesRepository.existsById(salarieId)) {
            throw new ExceptionResponse.ResourceNotFoundException("salarieId" + salarieId + " not found");
        }
        tacheRepository.findById(tacheRequest.getId()).map(tache -> {
            tache.setNom(tacheRequest.getNom());
            tache.setDescription(tacheRequest.getDescription());
            tache.setDelai((Timestamp) tacheRequest.getDelai());
            return tacheRepository.save(tache);
        });
    }

    @Override
    public Tache findTacheById(Long id) {
        return tacheRepository.findTacheById(id);
    }

}
