package com.ecommerce.order.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
public class OrderItemOutput {
    private UUID id;
    private UUID productId;
    private String name;
    private String description;
    private Integer quantity;
    private Double amount;
}