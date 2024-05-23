package com.tericcabrel.authapi.dtos;

import java.util.Set;

public class ShoppingCartDto {
    private Long shoppingCartId;
    private Double total;
    private Set<ShoppingCartProductDto> products;

    public Long getShoppingCartId() {
        return shoppingCartId;
    }

    public void setShoppingCartId(Long shoppingCartId) {
        this.shoppingCartId = shoppingCartId;
    }

    public Double getTotal() {
        return total;
    }

    public void setTotal(Double total) {
        this.total = total;
    }

    public Set<ShoppingCartProductDto> getProducts() {
        return products;
    }

    public void setProducts(Set<ShoppingCartProductDto> products) {
        this.products = products;
    }
}
