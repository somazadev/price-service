package com.ecommerce.price_service.domain;

import com.ecommerce.price_service.domain.model.Price;
import com.ecommerce.price_service.domain.service.ApplicablePriceResolver;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Currency;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class ApplicablePriceResolverTest {

    private static final LocalDateTime DATE = LocalDateTime.of(2020, 6, 14, 16, 0);

    private final ApplicablePriceResolver resolver = new ApplicablePriceResolver();

    private static Price price(int priceList, int priority, LocalDateTime start, LocalDateTime end) {
        return new Price(1L, 35455L, priceList, priority, start, end,
                new BigDecimal("10.00"), Currency.getInstance("EUR"));
    }

    @Test
    void returnsEmptyWhenThereAreNoCandidates() {
        assertThat(resolver.resolve(List.of(), DATE)).isEmpty();
    }

    @Test
    void returnsTheOnlyApplicableCandidate() {
        Price only = price(1, 0, DATE.minusDays(1), DATE.plusDays(1));

        assertThat(resolver.resolve(List.of(only), DATE)).contains(only);
    }

    @Test
    void picksTheHighestPriorityWhenRangesOverlap() {
        Price low = price(1, 0, DATE.minusDays(1), DATE.plusDays(1));
        Price high = price(2, 1, DATE.minusHours(1), DATE.plusHours(1));

        assertThat(resolver.resolve(List.of(low, high), DATE)).contains(high);
    }

    @Test
    void resultDoesNotDependOnInputOrder() {
        Price low = price(1, 0, DATE.minusDays(1), DATE.plusDays(1));
        Price high = price(2, 1, DATE.minusHours(1), DATE.plusHours(1));

        assertThat(resolver.resolve(List.of(high, low), DATE)).contains(high);
    }

    @Test
    void ignoresCandidatesNotApplicableAtTheDate() {
        Price applicable = price(1, 0, DATE.minusDays(1), DATE.plusDays(1));
        Price expiredButHigher = price(2, 5, DATE.minusDays(3), DATE.minusDays(2));

        assertThat(resolver.resolve(List.of(applicable, expiredButHigher), DATE)).contains(applicable);
    }

    @Test
    void breaksPriorityTiesByMostRecentStartDate() {
        Price older = price(1, 1, DATE.minusDays(2), DATE.plusDays(1));
        Price newer = price(2, 1, DATE.minusHours(1), DATE.plusDays(1));

        assertThat(resolver.resolve(List.of(older, newer), DATE)).contains(newer);
    }

    @Test
    void ignoresNullCandidates() {
        Price applicable = price(1, 0, DATE.minusDays(1), DATE.plusDays(1));

        assertThat(resolver.resolve(Arrays.asList(null, applicable), DATE)).contains(applicable);
    }
}
