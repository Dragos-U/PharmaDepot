package com.bearsoft.pharmadepot.repositories;

import com.bearsoft.pharmadepot.models.domain.entities.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long> {

    @Override
    List<Product> findAll();

    boolean existsByName(String name);

    Optional<Product> findByName(String name);
}
