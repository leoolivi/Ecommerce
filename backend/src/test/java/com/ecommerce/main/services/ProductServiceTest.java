// ========================================
// ProductServiceTest.java
// ========================================
package com.ecommerce.main.services;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.ArgumentMatchers.any;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;

import com.ecommerce.main.exceptions.ProductNotFoundException;
import com.ecommerce.main.models.Product;
import com.ecommerce.main.repositories.ProductRepository;

@ExtendWith(MockitoExtension.class)
@DisplayName("Product Service Tests")
class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductService productService;

    private Product testProduct;

    @BeforeEach
    void setUp() {
        testProduct = Product.builder()
                .id(1L)
                .name("Test Product")
                .description("Test Description")
                .price(99.99)
                .stockQuantity(10)
                .category("Electronics")
                .imageUrl("https://example.com/image.jpg")
                .build();
    }

    @Test
    @DisplayName("Should get all products")
    void testGetProducts() {
        // Arrange
        List<Product> expectedProducts = Arrays.asList(testProduct);
        when(productRepository.findAll()).thenReturn(expectedProducts);

        // Act
        List<Product> actualProducts = productService.getProducts();

        // Assert
        assertNotNull(actualProducts);
        assertEquals(1, actualProducts.size());
        assertEquals(testProduct.getName(), actualProducts.get(0).getName());
        verify(productRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Should add product successfully")
    void testAddProduct() {
        // Arrange
        when(productRepository.save(any(Product.class))).thenReturn(testProduct);

        // Act
        Product savedProduct = productService.addProduct(testProduct);

        // Assert
        assertNotNull(savedProduct);
        assertEquals(testProduct.getName(), savedProduct.getName());
        verify(productRepository, times(1)).save(testProduct);
    }

    @Test
    @DisplayName("Should find product by ID")
    void testFindProductById() throws ProductNotFoundException {
        // Arrange
        when(productRepository.findById(1L)).thenReturn(Optional.of(testProduct));

        // Act
        Product foundProduct = productService.findProductById(1L);

        // Assert
        assertNotNull(foundProduct);
        assertEquals(testProduct.getId(), foundProduct.getId());
        verify(productRepository, times(1)).findById(1L);
    }

    @Test
    @DisplayName("Should throw exception when product not found by ID")
    void testFindProductByIdNotFound() {
        // Arrange
        when(productRepository.findById(999L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ProductNotFoundException.class, () -> {
            productService.findProductById(999L);
        });
        verify(productRepository, times(1)).findById(999L);
    }

    @Test
    @DisplayName("Should find product by name")
    void testFindProductByName() throws ProductNotFoundException {
        // Arrange
        when(productRepository.findByName("Test Product")).thenReturn(Optional.of(testProduct));

        // Act
        Product foundProduct = productService.findProductByName("Test Product");

        // Assert
        assertNotNull(foundProduct);
        assertEquals(testProduct.getName(), foundProduct.getName());
        verify(productRepository, times(1)).findByName("Test Product");
    }

    @Test
    @DisplayName("Should throw exception when product not found by name")
    void testFindProductByNameNotFound() {
        // Arrange
        when(productRepository.findByName("Nonexistent")).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ProductNotFoundException.class, () -> {
            productService.findProductByName("Nonexistent");
        });
    }

    @Test
    @DisplayName("Should update product successfully")
    void testUpdateProduct() {
        // Arrange
        Product updatedProduct = Product.builder()
                .id(1L)
                .name("Updated Product")
                .price(149.99)
                .build();

        when(productRepository.findById(1L)).thenReturn(Optional.of(testProduct));
        when(productRepository.save(any(Product.class))).thenReturn(testProduct);

        // Act
        productService.updateProduct(updatedProduct);

        // Assert
        verify(productRepository, times(1)).findById(1L);
        verify(productRepository, times(1)).save(any(Product.class));
    }

    @Test
    @DisplayName("Should delete product successfully")
    void testDeleteProduct() {
        // Arrange
        doNothing().when(productRepository).deleteById(1L);

        // Act
        productService.deleteProduct(1L);

        // Assert
        verify(productRepository, times(1)).deleteById(1L);
    }
}