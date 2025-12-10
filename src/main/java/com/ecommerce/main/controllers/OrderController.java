package com.ecommerce.main.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
import com.ecommerce.main.exceptions.OrderNotFoundException;
import com.ecommerce.main.exceptions.SettingNotFoundException;
import com.ecommerce.main.facades.OrderFacade;
import com.ecommerce.main.services.OrderService;

import lombok.RequiredArgsConstructor;


@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
@CrossOrigin(origins= "http://localhost:5173")
public class OrderController {

    private final OrderService service;
    private final OrderFacade facade;
    
    @GetMapping("orders")
    public ResponseEntity<?> getOrders() {
        return ResponseEntity.ok(service.getOrders());
    }

    @GetMapping("order/{id}")
    public ResponseEntity<?> getOrderById(@PathVariable Long id) throws OrderNotFoundException {
        return ResponseEntity.ok(service.findOrderById(id));
    }

    @PostMapping("orders")
    public ResponseEntity<?> addOrder(@RequestBody OrderRequestDTO request) throws SettingNotFoundException, NumberFormatException, OrderNotFoundException, IllegalArgumentException {
        if (!facade.createOrder(request)) {
        return new ResponseEntity<>("There were some issues with payment", HttpStatus.BAD_REQUEST);
        }
        return ResponseEntity.ok("Order created successfully");
    }

    @PutMapping("order")
    public ResponseEntity<?> updateOrder(@RequestBody OrderRequestDTO request) throws OrderNotFoundException, SettingNotFoundException {
        return ResponseEntity.ok(service.updateOrder(request));
    }

    @DeleteMapping("order/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) throws OrderNotFoundException {
        service.deleteOrder(id);
        return ResponseEntity.ok("Order deleted successfully");
    }
    
}