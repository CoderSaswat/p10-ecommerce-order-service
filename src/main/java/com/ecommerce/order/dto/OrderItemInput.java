package com.ecommerce.order.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
public class OrderItemInput {
    private UUID productId;
    private Integer quantity;
    private Double amount;
}