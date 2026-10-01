package com.ecommerce.price_service.infrastructure.adapter.in.rest.mapper;

import com.ecommerce.price_service.domain.model.Price;
import com.ecommerce.price_service.infrastructure.adapter.in.rest.dto.PriceResponse;

public class PriceRestMapper {
    private PriceRestMapper() {
    }

    public static PriceResponse toResponse(Price price) {
        return new PriceResponse(
                price.productId(),
                price.brandId(),
                price.priceList(),
                price.startDate(),
                price.endDate(),
                price.price(),
                price.currency().getCurrencyCode()
        );
    }
}
