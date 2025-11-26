package com.ecommerce.main.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter @Setter
@AllArgsConstructor
public class AddProductRequest {
    private final String name;
    private final String description;
    private final Float price;
    private final Integer initialStockQuantity;
    private final String category;
}