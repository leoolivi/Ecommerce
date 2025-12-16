package com.ecommerce.main.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ecommerce.main.models.Order;

import java.util.List;
import java.util.Optional;


public interface OrderRepository extends JpaRepository<Order, Long> {
    public Optional<Order> findByCustomerId(Long customerId);
    public Optional<List<Order>> findAllByCustomerId(Long customerId);
}
