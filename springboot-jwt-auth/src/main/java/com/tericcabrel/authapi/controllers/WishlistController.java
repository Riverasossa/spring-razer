package com.tericcabrel.authapi.controllers;

import com.tericcabrel.authapi.dtos.ShoppingCartDto;
import com.tericcabrel.authapi.dtos.WishlistDto;
import com.tericcabrel.authapi.entities.ShoppingCart;
import com.tericcabrel.authapi.entities.User;
import com.tericcabrel.authapi.entities.Wishlist;
import com.tericcabrel.authapi.services.ShoppingCartService;
import com.tericcabrel.authapi.services.WishlistService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/wishlist")
public class WishlistController {
    private final WishlistService wishlistService;

    public WishlistController(WishlistService wishlistService) {
        this.wishlistService = wishlistService;
    }

    @PostMapping("/me/add-products")
    public ResponseEntity<Wishlist> addProductsToWishlist(@RequestBody Map<String, List<Long>> productIdsMap) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User currentUser = (User) authentication.getPrincipal();
        Long userId = currentUser.getId();

        List<Long> productIds = productIdsMap.get("productIds");
        if (productIds == null || productIds.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        Wishlist wishlist = wishlistService.getWishlistByUserId(userId);
        for (Long productId : productIds) {
            wishlistService.addProductToWishlist(userId, productId);
        }

        wishlist = wishlistService.getWishlistByUserId(userId);
        return new ResponseEntity<>(wishlist, HttpStatus.OK);
    }

    @DeleteMapping("/me/remove-product/{productId}")
    public ResponseEntity<WishlistDto> removeProductFromWishlist(@PathVariable Long productId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User currentUser = (User) authentication.getPrincipal();
        Long userId = currentUser.getId();

        WishlistDto wishlistDto = wishlistService.removeProductFromWishlist(userId, productId);
        return new ResponseEntity<>(wishlistDto, HttpStatus.OK);
    }

    @PatchMapping("/me/decrease-product-quantity/{productId}")
    public ResponseEntity<WishlistDto> decreaseProductQuantity(@PathVariable Long productId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User currentUser = (User) authentication.getPrincipal();
        Long userId = currentUser.getId();

        WishlistDto wishlistDto = wishlistService.decreaseProductQuantity(userId, productId);
        return new ResponseEntity<>(wishlistDto, HttpStatus.OK);
    }

    @DeleteMapping("/me/clear-wishlist")
    public ResponseEntity<WishlistDto> clearWishlist() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User currentUser = (User) authentication.getPrincipal();
        Long userId = currentUser.getId();

        WishlistDto wishlistDto = wishlistService.clearWishlist(userId);
        return new ResponseEntity<>(wishlistDto, HttpStatus.OK);
    }

    @GetMapping("/me")
    public ResponseEntity<WishlistDto> getWishlistByUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User currentUser = (User) authentication.getPrincipal();
        Long userId = currentUser.getId();

        WishlistDto wishlistDto = wishlistService.getWishlistDtoByUserId(userId);
        if (wishlistDto == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(wishlistDto, HttpStatus.OK);
    }
}
