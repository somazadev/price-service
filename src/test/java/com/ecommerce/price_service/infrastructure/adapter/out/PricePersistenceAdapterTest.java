package com.ecommerce.price_service.infrastructure.adapter.out;

import com.ecommerce.price_service.domain.model.Price;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.context.annotation.Import;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Import(PricePersistenceAdapter.class)
public class PricePersistenceAdapterTest {

    private static final Long BRAND_ID = 1L;
    private static final Long PRODUCT_ID = 35455L;

    @Autowired
    private PricePersistenceAdapter adapter;

    @ParameterizedTest(name = "{0} -> price lists [{1}]")
    @CsvSource(delimiter = ';', value = {
            "2020-06-14T10:00:00; 1",
            "2020-06-14T16:00:00; 1,2",
            "2020-06-14T21:00:00; 1",
            "2020-06-15T10:00:00; 1,3",
            "2020-06-16T21:00:00; 1,4",
            "2020-06-14T00:00:00; 1",
            "2020-12-31T23:59:59; 1,4"
    })
    void returnsEveryPriceWhoseRangeContainsTheDate(LocalDateTime date, String expectedPriceLists) {
        List<Integer> expected = Arrays.stream(expectedPriceLists.split(","))
                .map(String::trim)
                .map(Integer::valueOf)
                .toList();

        List<Price> prices = adapter.findApplicablePrices(BRAND_ID, PRODUCT_ID, date);

        assertThat(prices).extracting(Price::priceList).containsExactlyInAnyOrderElementsOf(expected);
    }

    @Test
    void mapsEveryFieldToTheDomainModel() {
        List<Price> prices = adapter.findApplicablePrices(BRAND_ID, PRODUCT_ID,
                LocalDateTime.of(2020, 6, 14, 10, 0));

        assertThat(prices).singleElement().satisfies(price -> {
            assertThat(price.brandId()).isEqualTo(BRAND_ID);
            assertThat(price.productId()).isEqualTo(PRODUCT_ID);
            assertThat(price.priceList()).isEqualTo(1);
            assertThat(price.priority()).isZero();
            assertThat(price.startDate()).isEqualTo(LocalDateTime.of(2020, 6, 14, 0, 0));
            assertThat(price.endDate()).isEqualTo(LocalDateTime.of(2020, 12, 31, 23, 59, 59));
            assertThat(price.price()).isEqualByComparingTo(new BigDecimal("35.50"));
            assertThat(price.currency().getCurrencyCode()).isEqualTo("EUR");
        });
    }

    @Test
    void returnsEmptyOutsideAnyRange() {
        assertThat(adapter.findApplicablePrices(BRAND_ID, PRODUCT_ID, LocalDateTime.of(2021, 1, 1, 0, 0)))
                .isEmpty();
    }

    @Test
    void returnsEmptyForUnknownBrandOrProduct() {
        LocalDateTime date = LocalDateTime.of(2020, 6, 14, 10, 0);

        assertThat(adapter.findApplicablePrices(2L, PRODUCT_ID, date)).isEmpty();
        assertThat(adapter.findApplicablePrices(BRAND_ID, 99999L, date)).isEmpty();
    }
}
