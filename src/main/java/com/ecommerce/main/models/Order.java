package com.ecommerce.main.models;

import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Builder
@Getter @Setter
@Table(name="_orders")
@AllArgsConstructor
@NoArgsConstructor
public class Order {
    @Id @GeneratedValue(strategy=GenerationType.AUTO)
    private Long id;
    
    @ManyToMany
    @JoinTable(
        name="order_product",
        joinColumns=@JoinColumn(name="order_id"),
        inverseJoinColumns=@JoinColumn(name="product_id")
    )
    private List<Product> products;
    
    @Column(unique=false)
    private Double subtotal;
    
    @Column(unique=false, nullable=false)
    private String shippingAddress;
    
    @Column(name="customer_id", unique=false, nullable=true)
    private Long customerId;
    
    @PrePersist
    @PreUpdate
    public void calculateSubtotal() {
        if (products != null && !products.isEmpty()) {
            subtotal = 0.0;
            for (Product product : products) {
                subtotal += product.getPrice();
            }
            subtotal = Double.valueOf(String.format("%.2f", subtotal).replace(",", "."));
        } else {
            subtotal = 0.0;
        }
    }
    
    public void calculateSubtotal(double shippingCost) {
        if (products != null && !products.isEmpty()) {
            subtotal = shippingCost;
            for (Product product : products) {
                subtotal += product.getPrice();
            }
            subtotal = Double.valueOf(String.format("%.2f", subtotal).replace(",", "."));
        } else {
            subtotal = shippingCost;
        }
    }
}