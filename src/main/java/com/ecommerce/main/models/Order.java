package com.ecommerce.main.models;

import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Entity
@Builder
@Getter @Setter
public class Order {
    @Id @GeneratedValue(strategy=GenerationType.AUTO)
    private Long id;
    @JoinColumn(unique=false, nullable=false)
    @ManyToOne
    private List<Product> products;
    @Column(unique=false, nullable=false)
    private double subtotal;
    private String shippingAddress;
    private Long customerId;

    public void calculateSubtotal(double shippingCost) {
        subtotal = 0;
        for (Product product: products) {
            subtotal += product.getPrice();
        }
        subtotal += shippingCost;
    }
}
