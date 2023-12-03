package com.ecommerce.order.serviceclient;



import com.ecommerce.order.dto.NotificationDto;
import com.ecommerce.order.dto.ProductDto;

import java.util.List;
import java.util.UUID;

public interface NotificationServiceClient {
    void createNotification(NotificationDto notificationDto);
}
