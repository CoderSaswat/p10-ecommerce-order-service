package com.ecommerce.order.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
public class OrderInput {
    private UUID userId;
    private List<OrderItemInput> orderItems;
    private Double totalAmount;
}
