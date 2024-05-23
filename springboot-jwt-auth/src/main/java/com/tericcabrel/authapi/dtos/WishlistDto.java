package com.tericcabrel.authapi.dtos;

import java.util.Set;

public class WishlistDto {
    private Long wishlistId;
    private Set<WishlistProductDto> products;

    public Long getWishlistId() {
        return wishlistId;
    }

    public void setWishlistId(Long wishlistId) {
        this.wishlistId = wishlistId;
    }

    public Set<WishlistProductDto> getProducts() {
        return products;
    }

    public void setProducts(Set<WishlistProductDto> products) {
        this.products = products;
    }
}
