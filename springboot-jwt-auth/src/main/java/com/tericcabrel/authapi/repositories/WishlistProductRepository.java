package com.tericcabrel.authapi.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import com.tericcabrel.authapi.entities.WishlistProduct;

public interface WishlistProductRepository extends JpaRepository<WishlistProduct, Long> {
}
