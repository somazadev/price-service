package com.ecommerce.price_service.application.port.in;

import com.ecommerce.price_service.application.exception.InvalidPriceQueryException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class GetApplicablePriceQueryTest {

    private static final LocalDateTime DATE = LocalDateTime.of(2020, 6, 14, 10, 0);

    @Test
    void createsQueryWhenAllValuesAreValid() {
        assertThatNoException().isThrownBy(() -> new GetApplicablePriceQuery(1L, 35455L, DATE));
    }

    @ParameterizedTest
    @NullSource
    @ValueSource(longs = {0, -1})
    void rejectsInvalidBrandId(Long brandId) {
        assertThatThrownBy(() -> new GetApplicablePriceQuery(brandId, 35455L, DATE))
                .isInstanceOf(InvalidPriceQueryException.class)
                .hasMessage("brandId must be a positive number");
    }

    @ParameterizedTest
    @NullSource
    @ValueSource(longs = {0, -1})
    void rejectsInvalidProductId(Long productId) {
        assertThatThrownBy(() -> new GetApplicablePriceQuery(1L, productId, DATE))
                .isInstanceOf(InvalidPriceQueryException.class)
                .hasMessage("productId must be a positive number");
    }

    @Test
    void rejectsNullApplicationDate() {
        assertThatThrownBy(() -> new GetApplicablePriceQuery(1L, 35455L, null))
                .isInstanceOf(InvalidPriceQueryException.class)
                .hasMessage("applicationDate must not be null");
    }
}