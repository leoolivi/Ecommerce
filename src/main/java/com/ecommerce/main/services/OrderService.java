package com.ecommerce.main.services;

import java.util.List;

import org.springframework.stereotype.Service;

import com.ecommerce.main.exceptions.OrderNotFoundException;
import com.ecommerce.main.models.Order;
import com.ecommerce.main.repositories.OrderRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OrderService {

    private OrderRepository repo;

    public List<Order> getOrders() {
        return repo.findAll();
    }

    public Order findOrderById(Long id) {
        return repo.findById(id).orElseThrow(() -> new OrderNotFoundException("Order not found"));
    }
    
    public Order addOrder(Order newOrder) {
        repo.save(newOrder);
        return newOrder;
    }

    public void deleteOrder(Long id) {
        repo.deleteById(id);
    }
}
