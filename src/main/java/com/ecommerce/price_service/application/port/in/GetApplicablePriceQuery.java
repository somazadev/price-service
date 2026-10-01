package com.ecommerce.price_service.application.port.in;

import java.time.LocalDateTime;
import java.util.Objects;

public record GetApplicablePriceQuery(Long brandId, Long productId, LocalDateTime applicationDate) {

    public GetApplicablePriceQuery {
        Objects.requireNonNull(brandId, "brandId must not be null");
        Objects.requireNonNull(productId, "productId must not be null");
        Objects.requireNonNull(applicationDate, "applicationDate must not be null");
    }
}
