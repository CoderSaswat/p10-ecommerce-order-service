package com.ecommerce.order.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
public class OrderOutput {
    private UUID id;
    private UUID userId;
    private List<OrderItemOutput> orderItems;
    private Double totalAmount; // Changed to Double
    private LocalDateTime orderDate;
}