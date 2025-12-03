package com.ecommerce.main.services;

import java.util.List;

import org.springframework.stereotype.Service;

import com.ecommerce.main.data.OrderRequestDTO;
import com.ecommerce.main.exceptions.OrderNotFoundException;
import com.ecommerce.main.exceptions.SettingNotFoundException;
import com.ecommerce.main.models.Order;
import com.ecommerce.main.models.Product;
import com.ecommerce.main.repositories.OrderRepository;
import com.ecommerce.main.repositories.ProductRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepo;
    private final ProductRepository productRepo;
    private final SettingService settingService;

    public List<Order> getOrders() {
        return orderRepo.findAll();
    }

    public Order findOrderById(Long id) throws OrderNotFoundException {
        return orderRepo.findById(id).orElseThrow(() -> new OrderNotFoundException("Order not found"));
    }
    
    public Order addOrder(OrderRequestDTO request) throws NumberFormatException, SettingNotFoundException {
        List<Product> products = productRepo.findAllById(request.getProductIds());
        Order newOrder = Order.builder().products(products).shippingAddress(request.getShippingAddress()).customerId(request.getCustomerId()).build();
        orderRepo.save(newOrder);
        newOrder.calculateSubtotal(Double.parseDouble(settingService.getValueByKey("shipping_fee")));
        return newOrder;
    }

    public void deleteOrder(Long id) throws OrderNotFoundException {
        orderRepo.findById(id).orElseThrow(() -> new OrderNotFoundException("Could not delete order with id " + id + " because it does not exist"));
        orderRepo.deleteById(id);
    }

    @Transactional
    public Order updateOrder(OrderRequestDTO request) throws OrderNotFoundException, NumberFormatException, SettingNotFoundException {
        List<Product> products = productRepo.findAllById(request.getProductIds());
        Order prevOrder = orderRepo.findById(request.getId()).orElseThrow(() -> new OrderNotFoundException("Order you want to edit does not exist!"));
        if (!products.isEmpty()) prevOrder.setProducts(products);
        if (!request.getShippingAddress().isBlank()) prevOrder.setShippingAddress(request.getShippingAddress());
        prevOrder.setCustomerId(request.getCustomerId());
        prevOrder.calculateSubtotal( Double.parseDouble(settingService.getValueByKey("shipping_fee")));
        orderRepo.save(prevOrder);
        return prevOrder;
    }

    @Transactional
    public void updateOrderStatus(OrderRequestDTO request) throws OrderNotFoundException {
        Order order = orderRepo.findById(request.getId()).orElseThrow(() -> new OrderNotFoundException("Order not found"));
        order.setStatus(request.getOrderStatus());
        orderRepo.save(order);
    }

}
