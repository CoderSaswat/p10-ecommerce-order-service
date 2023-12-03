package com.ecommerce.order.serviceclient.impl;

import com.ecommerce.order.dto.ProductDto;
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
public class ProductServiceClientImpl implements ProductServiceClient {
    private final RestTemplate restTemplate;

    @Value("${productServiceBaseUrl}")
    private String productServiceBaseUrl;


    public List<ProductDto> getProductsByIds(List<UUID> productIds) {
        String url = String.format("%s/products/details", productServiceBaseUrl); // Assuming "/products" is the endpoint for creating a product
        ParameterizedTypeReference<List<ProductDto>> responseType = new ParameterizedTypeReference<>() {};
        HttpEntity<List<UUID>> requestEntity = new HttpEntity<>(productIds);
        ResponseEntity<List<ProductDto>> responseEntity = restTemplate.exchange(
                url,
                HttpMethod.POST,
                requestEntity,
                responseType
        );
        return responseEntity.getBody();

    }
}
