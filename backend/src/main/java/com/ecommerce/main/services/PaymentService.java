package com.ecommerce.main.services;

import org.springframework.stereotype.Service;

import com.ecommerce.main.models.Order;
import com.ecommerce.main.models.PaymentMethod;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class PaymentService {

    public boolean processPayment(Order order) {
        PaymentMethod pm = order.getPayment();

        pm.validate();
        // In futuro: pm.process() → chiama Stripe
        // Mock:
        return true; // pagamento riuscito
    }
}
