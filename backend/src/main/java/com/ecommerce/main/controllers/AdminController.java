package com.ecommerce.main.controllers;

import java.util.List;

import javax.naming.OperationNotSupportedException;

import org.springframework.http.HttpStatus;
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
import org.springframework.web.bind.annotation.RestController;

import com.ecommerce.main.data.OrderRequestDTO;
import com.ecommerce.main.data.UserManagementDTO;
import com.ecommerce.main.data.UserResponseDTO;
import com.ecommerce.main.exceptions.OrderNotFoundException;
import com.ecommerce.main.exceptions.SettingNotFoundException;
import com.ecommerce.main.exceptions.UserAlreadyExistException;
import com.ecommerce.main.facades.OrderFacade;
import com.ecommerce.main.models.AppUser;
import com.ecommerce.main.models.Product;
import com.ecommerce.main.services.AppUserService;
import com.ecommerce.main.services.OrderService;
import com.ecommerce.main.services.ProductService;

import lombok.RequiredArgsConstructor;


@RestController
@RequestMapping("api/v1/admin")
@RequiredArgsConstructor
@CrossOrigin(origins= "http://localhost:5173")
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {

    private final OrderService orderService;
    private final OrderFacade orderFacade;
    private final AppUserService userService;
    private final ProductService productService;


    // Order management

    @GetMapping("orders")
    public ResponseEntity<?> getOrders() throws OrderNotFoundException {
        return ResponseEntity.ok(orderService.getOrders());
    }

    @GetMapping("orders/{id}")
    public ResponseEntity<?> getOrderById(@PathVariable Long id) throws OrderNotFoundException, OperationNotSupportedException {
        return ResponseEntity.ok(orderService.findOrderById(id));
    }

    @PostMapping("orders")
    public ResponseEntity<?> addOrder(@RequestBody OrderRequestDTO request) throws SettingNotFoundException, NumberFormatException, OrderNotFoundException, IllegalArgumentException {
        if (!orderFacade.createOrder(request)) {
        return new ResponseEntity<>("There were some issues with payment", HttpStatus.BAD_REQUEST);
        }
        return ResponseEntity.ok("Order created successfully");
    }

    @PutMapping("orders")
    public ResponseEntity<?> updateOrder(@RequestBody OrderRequestDTO request) throws OrderNotFoundException, SettingNotFoundException, NumberFormatException, OperationNotSupportedException {
        return ResponseEntity.ok(orderService.updateOrder(request));
    }

    @PutMapping("orders/{id}")
    public ResponseEntity<?> deleteOrder(@PathVariable Long id) throws OrderNotFoundException, OperationNotSupportedException {
        orderService.deleteOrder(id);
        return ResponseEntity.ok("Order deleted successfully");
    }

    // Products Management

    @PostMapping("products")
    public ResponseEntity<?> addProduct(@RequestBody Product request) {
        productService.addProduct(request);
        return ResponseEntity.ok("Product added successfully");
    }

    @PutMapping("products")
    public ResponseEntity<?> updateProduct(@RequestBody Product product) {
        productService.updateProduct(product);
        return ResponseEntity.ok("Product updated successfully");
    }

    @DeleteMapping("products/{id}")
    public ResponseEntity<?> deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
        return ResponseEntity.ok("Product deleted successfully");
    }

    // User Management

    // TODO: Testing

    @GetMapping("users")
    public List<AppUser> getUsers() {
        return userService.getUsers();
    }
    
    @GetMapping("users/{id}")
    public AppUser getUserById(@PathVariable Long id) {
        return userService.findUserById(id);
    }

    @PostMapping("users")
    public ResponseEntity<?> addUser(@RequestBody UserManagementDTO request) throws UserAlreadyExistException {
        AppUser newUser = userService.createUser(request);
        UserResponseDTO response = new UserResponseDTO(newUser.getId(), newUser.getEmail(), newUser.getRole());
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
    
    @PutMapping("users")
    public AppUser updateUser(@RequestBody UserManagementDTO request) {
        return userService.updateUser(request);
    }

    @DeleteMapping("users/{id}")
    public void deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
    }
}
