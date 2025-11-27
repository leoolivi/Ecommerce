package com.ecommerce.main.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;


@RestController
@RequestMapping("/api/v1")
public class OrderController {
    
    @GetMapping("orders")
    public ResponseEntity<?> getOrders() {
        return null;
    }
    
}
