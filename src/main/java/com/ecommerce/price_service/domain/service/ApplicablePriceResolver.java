package com.ecommerce.price_service.domain.service;

import com.ecommerce.price_service.domain.model.Price;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Comparator;
import java.util.Objects;
import java.util.Optional;

/**
 * Domain service that picks the price to apply among several candidates.
 * <p>
 * Rule: among the prices valid at the given date, the one with the highest priority wins.
 * Ties are broken by the most recent start date, so the result is always deterministic.
 */
public class ApplicablePriceResolver {

    private static final Comparator<Price> BY_PRECEDENCE = Comparator
            .comparing(Price::priority)
            .thenComparing(Price::startDate);

    public Optional<Price> resolve(Collection<Price> candidates, LocalDateTime applicationDate) {
        Objects.requireNonNull(candidates, "candidates must not be null");
        Objects.requireNonNull(applicationDate, "applicationDate must not be null");

        return candidates.stream()
                .filter(Objects::nonNull)
                .filter(price -> price.isApplicableAt(applicationDate))
                .max(BY_PRECEDENCE);
    }
}
