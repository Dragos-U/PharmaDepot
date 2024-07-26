package com.bearsoft.pharmadepot.repositories;

import com.bearsoft.pharmadepot.models.domain.entities.Category;
import com.bearsoft.pharmadepot.models.domain.enums.MedType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category, Long> {

    Optional<Category> findByMedType(MedType medType);
}
