package com.tericcabrel.authapi.dtos;

public class WishlistProductDto {
    private Long wishlistProductId;
    private ProductDto product;
    private Integer quantity;

    public Long getWishlistProductId() {
        return wishlistProductId;
    }

    public void setWishlistProductId(Long wishlistProductId) {
        this.wishlistProductId = wishlistProductId;
    }

    public ProductDto getProduct() {
        return product;
    }

    public void setProduct(ProductDto product) {
        this.product = product;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }
}
