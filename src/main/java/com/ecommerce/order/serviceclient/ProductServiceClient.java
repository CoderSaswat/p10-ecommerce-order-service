package com.ecommerce.order.serviceclient;



import com.ecommerce.order.dto.ProductDto;

import java.util.List;
import java.util.UUID;

public interface ProductServiceClient {
    List<ProductDto> getProductsByIds(List<UUID> productIds);
}
