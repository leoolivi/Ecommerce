package com.ecommerce.main.controllers;

import javax.naming.OperationNotSupportedException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
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
import com.ecommerce.main.models.AppUser;
import com.ecommerce.main.services.OrderService;

import lombok.RequiredArgsConstructor;


@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
@CrossOrigin(origins= "http://localhost:5173")
public class OrderController {

    private final OrderService service;
    private final OrderFacade facade;
    private final UserDetailsService userDetailsService;
    
    @GetMapping("orders")
    public ResponseEntity<?> getOrders(@AuthenticationPrincipal UserDetails principal) throws OrderNotFoundException {
        AppUser details = (AppUser) userDetailsService.loadUserByUsername(principal.getUsername());
        return ResponseEntity.ok(service.getOrdersOfCustomer(details.getId()));
    }

    @GetMapping("orders/{id}")
    public ResponseEntity<?> getOrderById(@AuthenticationPrincipal UserDetails principal, @PathVariable Long id) throws OrderNotFoundException, OperationNotSupportedException {
        AppUser details = (AppUser) userDetailsService.loadUserByUsername(principal.getUsername());
        return ResponseEntity.ok(service.findOrderByIdOfCustomer(id, details.getId()));
    }

    @PostMapping("orders")
    public ResponseEntity<?> addOrder(@AuthenticationPrincipal UserDetails principal, @RequestBody OrderRequestDTO request) throws SettingNotFoundException, NumberFormatException, OrderNotFoundException, IllegalArgumentException {
        AppUser details = (AppUser) userDetailsService.loadUserByUsername(principal.getUsername());
        request.setCustomerId(details.getId());
        if (!facade.createOrder(request)) {
        return new ResponseEntity<>("There were some issues with payment", HttpStatus.BAD_REQUEST);
        }
        return ResponseEntity.ok("Order created successfully");
    }

    @PutMapping("orders")
    public ResponseEntity<?> updateOrder(@AuthenticationPrincipal UserDetails principal, @RequestBody OrderRequestDTO request) throws OrderNotFoundException, SettingNotFoundException, NumberFormatException, OperationNotSupportedException {
        AppUser details = (AppUser) userDetailsService.loadUserByUsername(principal.getUsername());
        return ResponseEntity.ok(service.updateOrderOfCustomer(request, details.getId()));
    }

    @PatchMapping("orders/{id}")
    public ResponseEntity<?> cancelOrder(@AuthenticationPrincipal UserDetails principal, @PathVariable Long id) throws OrderNotFoundException, OperationNotSupportedException {
        AppUser details = (AppUser) userDetailsService.loadUserByUsername(principal.getUsername());
        service.cancelOrderOfCustomer(id, details.getId());
        return ResponseEntity.ok("Order canceled successfully");
    }
    
}