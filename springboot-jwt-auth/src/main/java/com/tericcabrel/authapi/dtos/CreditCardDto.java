package com.tericcabrel.authapi.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public class CreditCardDto {
    @NotBlank(message = "Card number is required")
    @Pattern(regexp = "^(4[0-9]{12}(?:[0-9]{3})?)$|^(5[1-5][0-9]{14})$|^(3[47][0-9]{13})$", message = "Invalid card number")
    private String cardNumber;

    @NotBlank(message = "Card holder name is required")
    @Pattern(regexp = "^[A-Za-z\\s]+$", message = "Card holder name must contain only letters and spaces")
    private String cardHolderName;

    @NotBlank(message = "Expiration date is required")
    @Pattern(regexp = "^(0[1-9]|1[0-2])/([0-9]{2})$", message = "Invalid expiration date format")
    private String expirationDate;

    @NotBlank(message = "CVV is required")
    @Pattern(regexp = "^[0-9]{3,4}$", message = "Invalid CVV")
    private String cvv;

    public @NotBlank(message = "Card number is required") @Pattern(regexp = "^(4[0-9]{12}(?:[0-9]{3})?)$|^(5[1-5][0-9]{14})$|^(3[47][0-9]{13})$", message = "Invalid card number") String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(@NotBlank(message = "Card number is required") @Pattern(regexp = "^(4[0-9]{12}(?:[0-9]{3})?)$|^(5[1-5][0-9]{14})$|^(3[47][0-9]{13})$", message = "Invalid card number") String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public @NotBlank(message = "Card holder name is required") @Pattern(regexp = "^[A-Za-z\\s]+$", message = "Card holder name must contain only letters and spaces") String getCardHolderName() {
        return cardHolderName;
    }

    public void setCardHolderName(@NotBlank(message = "Card holder name is required") @Pattern(regexp = "^[A-Za-z\\s]+$", message = "Card holder name must contain only letters and spaces") String cardHolderName) {
        this.cardHolderName = cardHolderName;
    }

    public @NotBlank(message = "Expiration date is required") @Pattern(regexp = "^(0[1-9]|1[0-2])/([0-9]{2})$", message = "Invalid expiration date format") String getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(@NotBlank(message = "Expiration date is required") @Pattern(regexp = "^(0[1-9]|1[0-2])/([0-9]{2})$", message = "Invalid expiration date format") String expirationDate) {
        this.expirationDate = expirationDate;
    }

    public @NotBlank(message = "CVV is required") @Pattern(regexp = "^[0-9]{3,4}$", message = "Invalid CVV") String getCvv() {
        return cvv;
    }

    public void setCvv(@NotBlank(message = "CVV is required") @Pattern(regexp = "^[0-9]{3,4}$", message = "Invalid CVV") String cvv) {
        this.cvv = cvv;
    }


}
