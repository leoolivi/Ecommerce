package com.ecommerce.main.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ecommerce.main.models.Order;
import com.ecommerce.main.services.OrderService;

import lombok.RequiredArgsConstructor;


@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService service;
    
    @GetMapping("orders")
    public ResponseEntity<?> getOrders() {
        return ResponseEntity.ok(service.getOrders());
    }

    @PostMapping("orders")
    public ResponseEntity<?> addOrder(Order newOrder) {
        return ResponseEntity.ok(service.addOrder(newOrder));
    }
    
}
