package com.tericcabrel.authapi.dtos;

public class ShoppingCartProductDto {
    private Long shopingCartProductId;
    private ProductDto product;
    private Integer quantity;

    public Long getShopingCartProductId() {
        return shopingCartProductId;
    }

    public void setShopingCartProductId(Long shopingCartProductId) {
        this.shopingCartProductId = shopingCartProductId;
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
