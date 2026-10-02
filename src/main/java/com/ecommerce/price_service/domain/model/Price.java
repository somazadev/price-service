package com.ecommerce.price_service.domain.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Currency;
import java.util.Objects;

public record Price(

        Long brandId,
                     Long productId,
                     Integer priceList,
                     Integer priority,
                     LocalDateTime startDate,
                     LocalDateTime endDate,
                     BigDecimal price,
                     Currency currency)
{ public Price {
    Objects.requireNonNull(brandId, "brandId must not be null");
    Objects.requireNonNull(productId, "productId must not be null");
    Objects.requireNonNull(priceList, "priceList must not be null");
    Objects.requireNonNull(priority, "priority must not be null");
    Objects.requireNonNull(startDate, "startDate must not be null");
    Objects.requireNonNull(endDate, "endDate must not be null");
    Objects.requireNonNull(price, "price must not be null");
    Objects.requireNonNull(currency, "currency must not be null");

    if (startDate.isAfter(endDate)) {
        throw new IllegalArgumentException("startDate must not be after endDate");
    }
    if (price.signum() < 0) {
        throw new IllegalArgumentException("price must not be negative");
    }
}
    public boolean isApplicableAt(LocalDateTime date) {
        Objects.requireNonNull(date, "date must not be null");
        return !date.isBefore(startDate) && !date.isAfter(endDate);
    }

}
