package com.ecommerce.price_service.application;

import com.ecommerce.price_service.application.port.in.GetApplicablePriceQuery;
import com.ecommerce.price_service.domain.exception.PriceNotFoundException;
import com.ecommerce.price_service.domain.model.Price;
import com.ecommerce.price_service.domain.repository.PriceRepository;
import com.ecommerce.price_service.domain.service.ApplicablePriceResolver;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Currency;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class GetApplicablePriceServiceTest {

    private static final Long BRAND_ID = 1L;
    private static final Long PRODUCT_ID = 35455L;
    private static final LocalDateTime DATE = LocalDateTime.of(2020, 6, 14, 16, 0);
    private static final GetApplicablePriceQuery QUERY = new GetApplicablePriceQuery(BRAND_ID, PRODUCT_ID, DATE);

    @Mock
    private PriceRepository priceRepository;

    private GetApplicablePriceService service;

    @BeforeEach
    void setUp() {
        service = new GetApplicablePriceService(priceRepository, new ApplicablePriceResolver());
    }

    private static Price price(int priceList, int priority, String price) {
        return new Price(BRAND_ID, PRODUCT_ID, priceList, priority,
                LocalDateTime.of(2020, 6, 14, 0, 0), LocalDateTime.of(2020, 12, 31, 23, 59, 59),
                new BigDecimal(price), Currency.getInstance("EUR"));
    }

    @Test
    void returnsTheHighestPriorityCandidate() {
        Price base = price(1, 0, "35.50");
        Price promo = price(2, 1, "25.45");
        when(priceRepository.findApplicablePrices(BRAND_ID, PRODUCT_ID, DATE)).thenReturn(List.of(base, promo));

        Price result = service.getApplicablePrice(QUERY);

        assertThat(result).isEqualTo(promo);
        verify(priceRepository).findApplicablePrices(BRAND_ID, PRODUCT_ID, DATE);
    }

    @Test
    void throwsWhenNoPriceApplies() {
        when(priceRepository.findApplicablePrices(BRAND_ID, PRODUCT_ID, DATE)).thenReturn(List.of());

        assertThatThrownBy(() -> service.getApplicablePrice(QUERY))
                .isInstanceOf(PriceNotFoundException.class)
                .hasMessageContaining("35455");
    }
}
