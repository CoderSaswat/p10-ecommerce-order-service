package com.ecommerce.order.controller;

import com.ecommerce.order.dto.OrderInput;
import com.ecommerce.order.dto.OrderOutput;
import com.ecommerce.order.service.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/order")
public class OrderController {
    private final OrderService orderService;

    @PostMapping
    public OrderOutput createOrder(@RequestBody OrderInput orderInput) {
        return orderService.createOrder(orderInput);
    }

    @GetMapping("/{orderId}")
    public OrderOutput getOrder(@PathVariable UUID orderId) {
        return orderService.getOrder(orderId);
    }

    @GetMapping("/user/{userId}")
    public List<OrderOutput> getOrdersByUser(@PathVariable UUID userId) {
        return orderService.getOrdersByUser(userId);
    }
}
