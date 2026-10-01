package com.ecommerce.price_service.domain;

import com.ecommerce.price_service.domain.model.Price;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Currency;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class PriceTest {

    private static final LocalDateTime START = LocalDateTime.of(2020, 6, 14, 15, 0);
    private static final LocalDateTime END = LocalDateTime.of(2020, 6, 14, 18, 30);

    private static Price price(LocalDateTime start, LocalDateTime end, BigDecimal amount) {
        return new Price(1L, 35455L, 2, 1, start, end, amount, Currency.getInstance("EUR"));
    }

    @Nested
    @DisplayName("isApplicableAt")
    class IsApplicableAt {

        private final Price price = price(START, END, new BigDecimal("25.45"));

        @Test
        void returnsTrueWhenDateIsInsideTheRange() {
            assertThat(price.isApplicableAt(START.plusHours(1))).isTrue();
        }

        @Test
        void includesTheStartBoundary() {
            assertThat(price.isApplicableAt(START)).isTrue();
        }

        @Test
        void includesTheEndBoundary() {
            assertThat(price.isApplicableAt(END)).isTrue();
        }

        @Test
        void returnsFalseBeforeTheRange() {
            assertThat(price.isApplicableAt(START.minusSeconds(1))).isFalse();
        }

        @Test
        void returnsFalseAfterTheRange() {
            assertThat(price.isApplicableAt(END.plusSeconds(1))).isFalse();
        }
    }

    @Nested
    @DisplayName("invariants")
    class Invariants {

        @Test
        void rejectsStartDateAfterEndDate() {
            assertThatThrownBy(() -> price(END, START, BigDecimal.TEN))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("startDate");
        }

        @Test
        void rejectsNegativeAmount() {
            assertThatThrownBy(() -> price(START, END, new BigDecimal("-0.01")))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("amount");
        }

        @Test
        void rejectsNullFields() {
            assertThatThrownBy(() -> price(null, END, BigDecimal.TEN))
                    .isInstanceOf(NullPointerException.class);
        }
    }
}