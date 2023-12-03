package com.ecommerce.order.service;


import com.ecommerce.order.dto.OrderInput;
import com.ecommerce.order.dto.OrderOutput;

import java.util.List;
import java.util.UUID;

public interface OrderService {
    OrderOutput createOrder(OrderInput orderInput);
    OrderOutput getOrder(UUID orderId);
    List<OrderOutput> getOrdersByUser(UUID userId);
}
