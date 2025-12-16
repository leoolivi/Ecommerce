package com.ecommerce.main.services;

import java.util.List;

import javax.naming.OperationNotSupportedException;

import org.springframework.stereotype.Service;

import com.ecommerce.main.data.OrderRequestDTO;
import com.ecommerce.main.exceptions.OrderNotFoundException;
import com.ecommerce.main.exceptions.SettingNotFoundException;
import com.ecommerce.main.models.Order;
import com.ecommerce.main.models.Product;
import com.ecommerce.main.models.enums.OrderStatus;
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

    public Order findOrderByIdOfCustomer(Long id, Long customerId) throws OrderNotFoundException, OperationNotSupportedException {
        Order order = orderRepo.findById(id).orElseThrow(() -> new OrderNotFoundException("Order not found"));
        if (!order.getCustomerId().equals(customerId)) throw new OperationNotSupportedException("Could not read another customer orders");
        return order;
    }

    public List<Order> getOrdersOfCustomer(Long customerId) throws OrderNotFoundException {
        return orderRepo.findAllByCustomerId(customerId).orElseThrow(() -> new OrderNotFoundException("Order not found"));
    } 
    
    public Order addOrder(OrderRequestDTO request) throws NumberFormatException, SettingNotFoundException {
        List<Product> products = productRepo.findAllById(request.getProductIds());
        Order newOrder = Order.builder().products(products).shippingAddress(request.getShippingAddress()).customerId(request.getCustomerId()).payment(request.getPaymentMethod()).build();
        newOrder.calculateSubtotal(Double.parseDouble(settingService.getValueByKey("shipping_fee")));
        newOrder = orderRepo.save(newOrder);
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
        if (request.getOrderStatus() != null ) prevOrder.setStatus(request.getOrderStatus());
        prevOrder.setCustomerId(request.getCustomerId());
        prevOrder.calculateSubtotal( Double.parseDouble(settingService.getValueByKey("shipping_fee")));
        orderRepo.save(prevOrder);
        return prevOrder;
    }

    @Transactional
    public Order updateOrderOfCustomer(OrderRequestDTO request, Long customerId) throws OrderNotFoundException, NumberFormatException, SettingNotFoundException, OperationNotSupportedException {
        Order order = orderRepo.findByCustomerId(customerId).orElseThrow(() -> new OrderNotFoundException("Order not found"));
        if (!order.getCustomerId().equals(customerId)) {
            throw new OperationNotSupportedException("Could not edit an order of another user.");
        }

        List<Product> products = productRepo.findAllById(request.getProductIds());
        Order prevOrder = orderRepo.findById(request.getId()).orElseThrow(() -> new OrderNotFoundException("Order you want to edit does not exist!"));
        if (!products.isEmpty()) prevOrder.setProducts(products);
        if (!request.getShippingAddress().isBlank()) prevOrder.setShippingAddress(request.getShippingAddress());
        if (request.getOrderStatus() != null ) prevOrder.setStatus(request.getOrderStatus());
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

    @Transactional
    public void cancelOrderOfCustomer(Long orderId, Long customerId) throws OrderNotFoundException, OperationNotSupportedException {
        Order order = orderRepo.findById(orderId).orElseThrow(() -> new OrderNotFoundException("Could not cancel order with id " + orderId + " because it does not exist"));
        if (!order.getCustomerId().equals(customerId)) throw new OperationNotSupportedException("Could not cancel and order of another user.");
        order.setStatus(OrderStatus.CANCELED);
        orderRepo.save(order);
    }

}
