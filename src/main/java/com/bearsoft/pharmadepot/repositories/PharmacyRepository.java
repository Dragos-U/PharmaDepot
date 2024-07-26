package com.bearsoft.pharmadepot.repositories;

import com.bearsoft.pharmadepot.models.domain.entities.Pharmacy;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PharmacyRepository extends JpaRepository<Pharmacy, Long> {

    Optional<Pharmacy> findPharmaciesByName(String name);
}
