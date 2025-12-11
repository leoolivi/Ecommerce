package com.ecommerce.main.controllers;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ecommerce.main.exceptions.ProductNotFoundException;
import com.ecommerce.main.models.Product;
import com.ecommerce.main.services.ProductService;

import lombok.RequiredArgsConstructor;


@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
@CrossOrigin(origins= "http://localhost:5173")
public class ProductController {

    private final ProductService productService;

    @GetMapping("products")
    public ResponseEntity<List<Product>> getProducts() {
        return ResponseEntity.ok(productService.getProducts());
    }

    @GetMapping("products/{id}")
    public ResponseEntity<Product> getProductById(@PathVariable Long id) throws ProductNotFoundException {
        return ResponseEntity.ok(productService.findProductById(id));
    }

    @GetMapping("products/search")
    public ResponseEntity<List<Product>> searchProduct(@RequestParam String q) {
        var products = productService.getProducts();
        var result = new ArrayList<Product>();
        for (Product product: products) {
            if(product.getName().toLowerCase().contains(q.toLowerCase()) || product.getName().toLowerCase().contains(q.toLowerCase()) ) result.add(product);
        }
        return ResponseEntity.ok(result);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("products")
    public ResponseEntity<?> addProduct(@RequestBody Product request) {
        productService.addProduct(request);
        return ResponseEntity.ok("Product added successfully");
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("products")
    public ResponseEntity<?> updateProduct(@RequestBody Product product) {
        productService.updateProduct(product);
        return ResponseEntity.ok("Product updated successfully");
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("products/{id}")
    public ResponseEntity<?> deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
        return ResponseEntity.ok("Product deleted successfully");
    }
    
}
