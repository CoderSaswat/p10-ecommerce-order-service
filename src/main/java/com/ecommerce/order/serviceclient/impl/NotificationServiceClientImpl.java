package com.ecommerce.order.serviceclient.impl;

import com.ecommerce.order.dto.NotificationDto;
import com.ecommerce.order.dto.ProductDto;
import com.ecommerce.order.serviceclient.NotificationServiceClient;
import com.ecommerce.order.serviceclient.ProductServiceClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class NotificationServiceClientImpl implements NotificationServiceClient {
    private final RestTemplate restTemplate;

    @Value("${notificationServiceBaseUrl}")
    private String notificationServiceBaseUrl;


    @Override
    public void createNotification(NotificationDto notificationDto) {
        String url = String.format("%s/notification", notificationServiceBaseUrl); // Assuming "/products" is the endpoint for creating a product
        ParameterizedTypeReference<NotificationDto> responseType = new ParameterizedTypeReference<>() {};
        HttpEntity<NotificationDto> requestEntity = new HttpEntity<>(notificationDto);
        restTemplate.exchange(
                url,
                HttpMethod.POST,
                requestEntity,
                responseType
        );
    }
}
