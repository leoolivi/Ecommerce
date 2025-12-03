package com.ecommerce.main.models;

import com.ecommerce.main.models.enums.PaymentType;

import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Embeddable
@AllArgsConstructor
@NoArgsConstructor
@Getter @Setter
public class PaymentMethod {

    @Enumerated(EnumType.STRING)
    private PaymentType type;
    
    private String cardNumber;
    private String cardHolder;
    private String expiryMonth;
    private String expiryYear;
    private String cvc;

    public void validate() {
        // controlli finti per ora
        if (cardNumber == null || cardNumber.length() < 12) {
            throw new IllegalArgumentException("Invalid card number");
        }
    }

    public String mask() {
        if (cardNumber == null || cardNumber.length() < 4) return "****";
        return "**** **** **** " + cardNumber.substring(cardNumber.length() - 4);
    }

    // Questo è volutamente "vuoto" ora
    public void process() {
        // in futuro parlerà con Stripe/PayPal, ecc.
    }
}
