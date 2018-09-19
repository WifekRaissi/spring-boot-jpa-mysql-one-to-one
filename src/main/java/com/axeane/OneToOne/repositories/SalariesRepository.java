package com.axeane.OneToOne.repositories;

import com.axeane.OneToOne.model.Salarie;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SalariesRepository extends JpaRepository<Salarie, Long> {
    List<Salarie> findSalarieByNom(String nom);

    Salarie findSalarieById(Long id);

}
