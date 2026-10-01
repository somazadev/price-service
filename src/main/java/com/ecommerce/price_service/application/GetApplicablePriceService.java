package com.ecommerce.price_service.application;

import com.ecommerce.price_service.application.port.in.GetApplicablePriceQuery;
import com.ecommerce.price_service.application.port.in.GetApplicablePriceUseCase;
import com.ecommerce.price_service.domain.exception.PriceNotFoundException;
import com.ecommerce.price_service.domain.model.Price;
import com.ecommerce.price_service.domain.repository.PriceRepository;
import com.ecommerce.price_service.domain.service.ApplicablePriceResolver;

import java.util.List;
import java.util.Objects;

public class GetApplicablePriceService implements GetApplicablePriceUseCase {

    private final PriceRepository priceRepository;
    private final ApplicablePriceResolver priceResolver;

    public GetApplicablePriceService(PriceRepository priceRepository, ApplicablePriceResolver priceResolver) {
        this.priceRepository = Objects.requireNonNull(priceRepository);
        this.priceResolver = Objects.requireNonNull(priceResolver);
    }

    @Override
    public Price getApplicablePrice(GetApplicablePriceQuery query) {
        List<Price> candidates = priceRepository.findApplicablePrices(
                query.brandId(), query.productId(), query.applicationDate());

        return priceResolver.resolve(candidates, query.applicationDate())
                .orElseThrow(() -> new PriceNotFoundException(
                        query.brandId(), query.productId(), query.applicationDate()));
    }
}
