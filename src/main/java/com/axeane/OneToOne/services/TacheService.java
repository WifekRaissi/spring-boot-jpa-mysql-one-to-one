package com.axeane.OneToOne.services;

import com.axeane.OneToOne.model.Tache;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface TacheService {

    void addTache(Long salarieId, Tache tache);

    Page<Tache> getAllTachesBySalarietId(Long salarieId,
                                         Pageable pageable);

    void deleteTache(Long salarieId, Long tacheId);

    void updateTache(Long salarieId, Tache tacheRequest);

    Tache findTacheById(Long id);
}
