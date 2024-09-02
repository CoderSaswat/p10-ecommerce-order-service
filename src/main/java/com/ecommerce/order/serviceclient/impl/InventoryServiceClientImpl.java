package com.ecommerce.order.serviceclient.impl;

import com.ecommerce.order.dto.ProductAvailabilityDto;
import com.ecommerce.order.dto.ProductDto;
import com.ecommerce.order.serviceclient.InventoryServiceClient;
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
public class InventoryServiceClientImpl implements InventoryServiceClient {
    private final RestTemplate restTemplate;

    @Value("${inventoryServiceBaseUrl}")
    private String inventoryServiceBaseUrl;

    @Override
    public void updateProductAvailability(ProductAvailabilityDto productAvailabilityDto) {
        String url = String.format("%s/inventory/update-product-availability", inventoryServiceBaseUrl);
        ParameterizedTypeReference<ProductDto> responseType = new ParameterizedTypeReference<>() {};
        HttpEntity<ProductAvailabilityDto> requestEntity = new HttpEntity<>(productAvailabilityDto);
        restTemplate.exchange(
                url,
                HttpMethod.POST,
                requestEntity,
                responseType
        );
    }
}
