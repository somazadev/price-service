package com.ecommerce.price_service.application.port.in;

import com.ecommerce.price_service.application.exception.InvalidPriceQueryException;

import java.time.LocalDateTime;

public record GetApplicablePriceQuery(Long brandId, Long productId, LocalDateTime applicationDate) {

    public GetApplicablePriceQuery {
        requirePositive(brandId, "brandId");
        requirePositive(productId, "productId");
        if (applicationDate == null) {
            throw new InvalidPriceQueryException("applicationDate must not be null");
        }
    }

    private static void requirePositive(Long value, String name) {
        if (value == null || value <= 0) {
            throw new InvalidPriceQueryException(name + " must be a positive number");
        }
    }
}
