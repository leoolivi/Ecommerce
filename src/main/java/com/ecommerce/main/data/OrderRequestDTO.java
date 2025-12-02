package com.ecommerce.main.data;

import java.util.List;

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
    private List<Long> productIds;  // Solo gli ID dei prodotti
    private String shippingAddress;
    private Long customerId;
}
