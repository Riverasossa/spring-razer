package com.tericcabrel.authapi.dtos;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class OrderProductDto {
    private Long orderProductId;
    private ProductDto product;
    private Integer quantity;

    public OrderProductDto(Long orderProductId, ProductDto product, Integer quantity) {
        this.orderProductId = orderProductId;
        this.product = product;
        this.quantity = quantity;
    }

    public Long getOrderProductId() {
        return orderProductId;
    }

    public void setOrderProductId(Long orderProductId) {
        this.orderProductId = orderProductId;
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
