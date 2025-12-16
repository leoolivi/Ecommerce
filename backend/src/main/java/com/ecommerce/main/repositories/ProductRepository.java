package com.ecommerce.main.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ecommerce.main.models.Product;


public interface ProductRepository extends JpaRepository<Product, Long>{
    public Optional<Product> findByName(String name);
}
