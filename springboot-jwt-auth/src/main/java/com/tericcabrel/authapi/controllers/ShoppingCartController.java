package com.tericcabrel.authapi.controllers;

import com.tericcabrel.authapi.dtos.ShoppingCartDto;
import com.tericcabrel.authapi.entities.ShoppingCart;
import com.tericcabrel.authapi.entities.User;
import com.tericcabrel.authapi.services.ShoppingCartService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/shopping-cart")
public class ShoppingCartController {
    private final ShoppingCartService shoppingCartService;

    public ShoppingCartController(ShoppingCartService shoppingCartService) {
        this.shoppingCartService = shoppingCartService;
    }

    @PostMapping("/me/add-products")
    public ResponseEntity<ShoppingCart> addProductsToCart(@RequestBody Map<String, List<Long>> productIdsMap) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User currentUser = (User) authentication.getPrincipal();
        Long userId = currentUser.getId();

        List<Long> productIds = productIdsMap.get("productIds");
        if (productIds == null || productIds.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        ShoppingCart shoppingCart = shoppingCartService.getShoppingCartByUserId(userId);
        for (Long productId : productIds) {
            shoppingCartService.addProductToCart(userId, productId);
        }

        shoppingCart = shoppingCartService.getShoppingCartByUserId(userId);
        return new ResponseEntity<>(shoppingCart, HttpStatus.OK);
    }

    @DeleteMapping("/me/remove-product/{productId}")
    public ResponseEntity<ShoppingCartDto> removeProductFromCart(@PathVariable Long productId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User currentUser = (User) authentication.getPrincipal();
        Long userId = currentUser.getId();

        ShoppingCartDto shoppingCartDto = shoppingCartService.removeProductFromCart(userId, productId);
        return new ResponseEntity<>(shoppingCartDto, HttpStatus.OK);
    }

    @PatchMapping("/me/decrease-product-quantity/{productId}")
    public ResponseEntity<ShoppingCartDto> decreaseProductQuantity(@PathVariable Long productId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User currentUser = (User) authentication.getPrincipal();
        Long userId = currentUser.getId();

        ShoppingCartDto shoppingCartDto = shoppingCartService.decreaseProductQuantity(userId, productId);
        return new ResponseEntity<>(shoppingCartDto, HttpStatus.OK);
    }

    @DeleteMapping("/me/clear-cart")
    public ResponseEntity<ShoppingCartDto> clearCart() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User currentUser = (User) authentication.getPrincipal();
        Long userId = currentUser.getId();

        ShoppingCartDto shoppingCartDto = shoppingCartService.clearCart(userId);
        return new ResponseEntity<>(shoppingCartDto, HttpStatus.OK);
    }

    @GetMapping("/me")
    public ResponseEntity<ShoppingCartDto> getShoppingCartByUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User currentUser = (User) authentication.getPrincipal();
        Long userId = currentUser.getId();

        ShoppingCartDto shoppingCartDto = shoppingCartService.getShoppingCartDtoByUserId(userId);
        if (shoppingCartDto == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(shoppingCartDto, HttpStatus.OK);
    }
}
