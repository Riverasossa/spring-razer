package com.tericcabrel.authapi.repositories;

import com.tericcabrel.authapi.entities.ShoppingCart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ShoppingCartRepository extends JpaRepository<ShoppingCart, Long> {
    @Query("SELECT sc FROM ShoppingCart sc JOIN FETCH sc.shoppingCartProducts WHERE sc.user.id = :userId")
    ShoppingCart findByUserIdWithProducts(@Param("userId") Long userId);
}