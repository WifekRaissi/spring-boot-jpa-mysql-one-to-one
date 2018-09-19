package com.axeane.OneToOne.repositories;

import com.axeane.OneToOne.model.Tache;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TacheRepository extends JpaRepository<Tache, Long> {
    List<Tache> findTacheByNom(String nom);

    Tache findTacheById(Long id);

    Page<Tache> findBySalarieId(Long salarieId, Pageable pageable);

}
