package com.ecommerce.order.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ProductAvailabilityDto {
    private String productName;
    private Integer amountSold;
}
