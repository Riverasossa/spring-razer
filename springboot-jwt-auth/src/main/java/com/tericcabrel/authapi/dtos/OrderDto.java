package com.tericcabrel.authapi.dtos;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.tericcabrel.authapi.entities.OrderStatus;

import java.util.Set;
import java.util.Date;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class OrderDto {

    private Long OrderId;

    private UserDto user;

    private Date orderDate;

    private OrderStatus status;

    private double subtotal;

    private double shippingCoast;

    private double taxes;

    private double total;

    @NotBlank(message = "Address is required")
    private String address;

    private String address2;

    @NotBlank(message = "Province is required")
    private String province;

    @NotBlank(message = "Canton is required")
    private String canton;

    @NotBlank(message = "District is required")
    private String district;

    @NotNull(message = "Zip code is required")
    @Positive(message = "Zip code must be positive")
    private Integer zipCode;

    @Valid
    private CreditCardDto creditCard;

    private String card;

    private Set<OrderProductDto> orderProducts;

    public @NotBlank(message = "Address is required") String getAddress() {
        return address;
    }

    public void setAddress(@NotBlank(message = "Address is required") String address) {
        this.address = address;
    }

    public String getAddress2() {
        return address2;
    }

    public void setAddress2(String address2) {
        this.address2 = address2;
    }

    public @NotBlank(message = "Province is required") String getProvince() {
        return province;
    }

    public void setProvince(@NotBlank(message = "Province is required") String province) {
        this.province = province;
    }

    public @NotBlank(message = "Canton is required") String getCanton() {
        return canton;
    }

    public void setCanton(@NotBlank(message = "Canton is required") String canton) {
        this.canton = canton;
    }

    public @NotBlank(message = "District is required") String getDistrict() {
        return district;
    }

    public void setDistrict(@NotBlank(message = "District is required") String district) {
        this.district = district;
    }

    public @NotNull(message = "Zip code is required") @Positive(message = "Zip code must be positive") Integer getZipCode() {
        return zipCode;
    }

    public void setZipCode(@NotNull(message = "Zip code is required") @Positive(message = "Zip code must be positive") Integer zipCode) {
        this.zipCode = zipCode;
    }

    public CreditCardDto getCreditCard() {
        return creditCard;
    }

    public void setCreditCard(CreditCardDto creditCard) {
        this.creditCard = creditCard;
    }
    public Long getOrderId() {
        return OrderId;
    }

    public void setOrderId(Long orderId) {
        OrderId = orderId;
    }

    public UserDto getUser() {
        return user;
    }

    public void setUser(UserDto user) {
        this.user = user;
    }

    public Date getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(Date orderDate) {
        this.orderDate = orderDate;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }

    public Set<OrderProductDto> getOrderProducts() {
        return orderProducts;
    }

    public void setOrderProducts(Set<OrderProductDto> orderProducts) {
        this.orderProducts = orderProducts;
    }

    public double getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(double subtotal) {
        this.subtotal = subtotal;
    }

    public double getShippingCoast() {
        return shippingCoast;
    }

    public void setShippingCoast(double shippingCoast) {
        this.shippingCoast = shippingCoast;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public double getTaxes() {
        return taxes;
    }

    public String getCard() {
        return card;
    }

    public void setCard(String card) {
        this.card = card;
    }

    public void setTaxes(double taxes) {
        this.taxes = taxes;
    }
}
