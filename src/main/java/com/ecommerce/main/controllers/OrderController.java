package com.ecommerce.main.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ecommerce.main.data.OrderRequestDTO;
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

    @GetMapping("order/{id}")
    public ResponseEntity<?> getOrderById(@PathVariable Long id) {
        return ResponseEntity.ok(service.findOrderById(id));
    }

    @PostMapping("orders")
    public ResponseEntity<?> addOrder(@RequestBody OrderRequestDTO request) {
        return ResponseEntity.ok(service.addOrder(request));
    }

    @PutMapping("order")
    public ResponseEntity<?> updateOrder(@RequestBody OrderRequestDTO request) {
        return ResponseEntity.ok(service.updateOrder(request));
    }

    @DeleteMapping("order/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        service.deleteOrder(id);
        return ResponseEntity.ok("Order deleted successfully");
    }
    
}
