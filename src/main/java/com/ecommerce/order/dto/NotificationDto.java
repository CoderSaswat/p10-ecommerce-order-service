package com.ecommerce.order.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class NotificationDto {
    private UUID userId;
    private String message;
}
