package com.tericcabrel.authapi.repositories;

import com.tericcabrel.authapi.entities.ShoppingCart;
import com.tericcabrel.authapi.entities.ShoppingCartProduct;
import com.tericcabrel.authapi.entities.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ShoppingCartProductRepository extends JpaRepository<ShoppingCartProduct, Long> {
/*
    Optional<ShoppingCartProduct> findByShoppingCartAndProduct(ShoppingCart shoppingCart, Product product);
    void deleteAllByShoppingCart(ShoppingCart shoppingCart);
*/
}
