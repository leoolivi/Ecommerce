package com.ecommerce.main.facades;

import org.springframework.stereotype.Service;

import com.ecommerce.main.data.OrderRequestDTO;
import com.ecommerce.main.exceptions.OrderNotFoundException;
import com.ecommerce.main.exceptions.SettingNotFoundException;
import com.ecommerce.main.models.enums.OrderStatus;
import com.ecommerce.main.services.OrderService;
import com.ecommerce.main.services.PaymentService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OrderFacade {
    
    private final OrderService orderService;
    private final PaymentService paymentService;

    public boolean createOrder(OrderRequestDTO request) throws NumberFormatException, SettingNotFoundException, OrderNotFoundException {
        var order = orderService.addOrder(request);
        orderService.updateOrderStatus(OrderRequestDTO.builder().id(order.getId()).orderStatus(OrderStatus.PAYING).build());
        if (paymentService.processPayment(order)) {
            orderService.updateOrderStatus(OrderRequestDTO.builder().id(order.getId()).orderStatus(OrderStatus.PAID).build());
            return true;
        }
        return false;
    }
}
