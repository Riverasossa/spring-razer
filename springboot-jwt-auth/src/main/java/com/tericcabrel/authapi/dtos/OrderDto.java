package com.tericcabrel.authapi.dtos;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

public class OrderDto {
    @NotBlank(message = "Address is required")
    private String address;

    @NotBlank
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

    @NotNull(message = "Card is required")
    @Positive(message = "Card must be positive")
    private Integer card;

    @Valid
    private CreditCardDto creditCard;

    public @NotBlank(message = "Address is required") String getAddress() {
        return address;
    }

    public void setAddress(@NotBlank(message = "Address is required") String address) {
        this.address = address;
    }

    public @NotBlank String getAddress2() {
        return address2;
    }

    public void setAddress2(@NotBlank String address2) {
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

    public @NotNull(message = "Card is required") @Positive(message = "Card must be positive") Integer getCard() {
        return card;
    }

    public void setCard(@NotNull(message = "Card is required") @Positive(message = "Card must be positive") Integer card) {
        this.card = card;
    }

    public @Valid CreditCardDto getCreditCard() {
        return creditCard;
    }

    public void setCreditCard(@Valid CreditCardDto creditCard) {
        this.creditCard = creditCard;
    }
}
