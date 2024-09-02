package com.ecommerce.order.serviceclient;



import com.ecommerce.order.dto.ProductAvailabilityDto;
import com.ecommerce.order.dto.ProductDto;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.UUID;

public interface InventoryServiceClient {
    void updateProductAvailability(ProductAvailabilityDto productAvailabilityDto);
}
