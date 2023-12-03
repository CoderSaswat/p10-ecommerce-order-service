package com.ecommerce.order.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@Document("orders")
public class Order {
    @Id
    private UUID id;
    private UUID userId;
    private List<OrderItem> orderItems;
    private Double totalAmount;
    private LocalDateTime orderDate;
}
