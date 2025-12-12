package com.ecommerce.main.services;

import java.util.List;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.ecommerce.main.exceptions.ProductNotFoundException;
import com.ecommerce.main.models.Product;
import com.ecommerce.main.repositories.ProductRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class ProductService {
    
    private final ProductRepository repo;

    public List<Product> getProducts() {
        return repo.findAll();
    }

    public Product addProduct(Product newProduct) {
        repo.save(newProduct);
        return newProduct;
    }

    public Product findProductById(Long id) throws ProductNotFoundException {
        return repo.findById(id).orElseThrow(() -> new ProductNotFoundException("Product not found"));
    }

    public Product findProductByName(String name) throws ProductNotFoundException {
        return repo.findByName(name).orElseThrow(() -> new ProductNotFoundException("Product not found"));
    }

    @Transactional
    public void updateProduct(Product product) {
        Product currProduct = repo.findById(product.getId()).orElseThrow(() -> new UsernameNotFoundException("Product not found"));
        if (product.getName() != null) currProduct.setName(product.getName());
        if (product.getDescription()!= null) currProduct.setDescription(product.getDescription());
        if (product.getPrice() != null) currProduct.setPrice(product.getPrice());
        if (product.getStockQuantity() != null) currProduct.setStockQuantity(product.getStockQuantity());
        if (product.getCategory() != null) currProduct.setCategory(product.getCategory());
        repo.save(currProduct);
    }

    public void deleteProduct(Long id) {
        repo.deleteById(id);
    }

}
