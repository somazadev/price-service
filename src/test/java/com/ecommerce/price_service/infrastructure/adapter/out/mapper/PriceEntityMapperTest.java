package com.ecommerce.price_service.infrastructure.adapter.out.mapper;

import com.ecommerce.price_service.domain.model.Price;
import com.ecommerce.price_service.infrastructure.adapter.out.entity.PriceEntity;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Currency;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class PriceEntityMapperTest {

    private static final LocalDateTime START = LocalDateTime.of(2020, 6, 14, 15, 0);
    private static final LocalDateTime END = LocalDateTime.of(2020, 6, 14, 18, 30);

    private static PriceEntity entityWithCurrency(String currency) {
        return new PriceEntity(1L, 35455L, 2, 1, START, END, new BigDecimal("25.45"), currency);
    }

    @Test
    void mapsEveryFieldOfTheEntityToTheDomainModel() {
        Price price = PriceEntityMapper.toDomain(entityWithCurrency("EUR"));

        assertThat(price.brandId()).isEqualTo(1L);
        assertThat(price.productId()).isEqualTo(35455L);
        assertThat(price.priceList()).isEqualTo(2);
        assertThat(price.priority()).isEqualTo(1);
        assertThat(price.startDate()).isEqualTo(START);
        assertThat(price.endDate()).isEqualTo(END);
        assertThat(price.price()).isEqualByComparingTo("25.45");
        assertThat(price.currency()).isEqualTo(Currency.getInstance("EUR"));
    }

    @Test
    void trimsPaddedCurrencyCodes() {
        Price price = PriceEntityMapper.toDomain(entityWithCurrency("EUR "));

        assertThat(price.currency()).isEqualTo(Currency.getInstance("EUR"));
    }

    @Test
    void rejectsUnknownCurrencyCodes() {
        assertThatThrownBy(() -> PriceEntityMapper.toDomain(entityWithCurrency("ABC")))
                .isInstanceOf(IllegalArgumentException.class);
    }
}