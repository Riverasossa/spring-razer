package com.tericcabrel.authapi.repositories;

import com.tericcabrel.authapi.entities.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    Optional<Product> findByName(String name);
/*    Page<Product> findByNameContaining(String name, Pageable pageable);
    Page<Product> findByCategoryIn(List<String> categories, Pageable pageable);
    Page<Product> findByPriceBetween(double minPrice, double maxPrice, Pageable pageable);*/
}
