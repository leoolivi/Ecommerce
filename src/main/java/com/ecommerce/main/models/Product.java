package com.ecommerce.main.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Builder
@Table(name="products")
@Setter 
@Getter
@Entity
@AllArgsConstructor
@NoArgsConstructor(force = true)
public class Product {
    @Id @GeneratedValue(strategy=GenerationType.AUTO)
    private Long id;
    @Column(unique=true, nullable=false)
    private String name;
    @Column(nullable=true)
    private String description;
    @Column(nullable=false)
    private Float price;
    @Column(nullable=false)
    private Integer stockQuantity;
    @Column(nullable=false)
    private String category;
    @Column(nullable=false)
    private String imageUrl;
}
