package com.ecommerce.main.data;

import java.util.List;

import com.ecommerce.main.models.PaymentMethod;
import com.ecommerce.main.models.enums.OrderStatus;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrderRequestDTO {
    private Long id;
    private List<Long> productIds;
    private String shippingAddress;
    private Long customerId;
    private PaymentMethod paymentMethod;
    private OrderStatus orderStatus;
}
