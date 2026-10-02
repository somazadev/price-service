package com.ecommerce.price_service.infrastructure.adapter.in.rest.mapper;

import com.ecommerce.price_service.domain.model.Price;
import com.ecommerce.price_service.infrastructure.adapter.in.rest.dto.PriceResponse;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Currency;

import static org.assertj.core.api.Assertions.assertThat;

class PriceRestMapperTest {

    private static final LocalDateTime START = LocalDateTime.of(2020, 6, 14, 15, 0);
    private static final LocalDateTime END = LocalDateTime.of(2020, 6, 14, 18, 30);

    @Test
    void mapsEveryFieldOfThePriceToTheResponse() {
        Price price = new Price(1L, 35455L, 2, 1, START, END,
                new BigDecimal("25.45"), Currency.getInstance("EUR"));

        PriceResponse response = PriceRestMapper.toResponse(price);

        assertThat(response.productId()).isEqualTo(35455L);
        assertThat(response.brandId()).isEqualTo(1L);
        assertThat(response.priceList()).isEqualTo(2);
        assertThat(response.startDate()).isEqualTo(START);
        assertThat(response.endDate()).isEqualTo(END);
        assertThat(response.price()).isEqualByComparingTo("25.45");
        assertThat(response.currency()).isEqualTo("EUR");
    }
}