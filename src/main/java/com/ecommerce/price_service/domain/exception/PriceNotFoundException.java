package com.ecommerce.price_service.domain.exception;

import java.io.Serial;
import java.time.LocalDateTime;

public class PriceNotFoundException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = 1L;

    public PriceNotFoundException(Long brandId, Long productId, LocalDateTime applicationDate) {
        super("No applicable price found for brand %d, product %d at %s"
                .formatted(brandId, productId, applicationDate));
    }
}
