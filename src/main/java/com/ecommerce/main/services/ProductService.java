package com.ecommerce.main.services;

import java.util.List;
import java.util.Optional;

import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.ecommerce.main.models.Product;
import com.ecommerce.main.repositories.ProductRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProductService {
    
    private final ProductRepository repo;

    public List<Product> getProducts() {
        return repo.findAll();
    }

    public Product addProduct(Product newProduct) {
        repo.save(newProduct);
        return newProduct;
    }

    public Optional<Product> findProductById(Long id) {
        return repo.findById(id);
    }

    public Optional<Product> findProductByName(String name) {
        return repo.findByName(name);
    }

    public void updateProduct(Product product) {
        Product currProduct = repo.findById(product.getId()).orElseThrow(() -> new UsernameNotFoundException("Product not found"));
        if (product.getName() != null) currProduct.setName(product.getName());
        if (product.getDescription()!= null) currProduct.setDescription(product.getDescription());
        if (product.getPrice() != null) currProduct.setPrice(product.getPrice());
        if (product.getStockQuantity() != null) currProduct.setStockQuantity(product.getStockQuantity());
        if (product.getCategory() != null) currProduct.setCategory(product.getCategory());
        repo.save(currProduct);
    }

}
