package com.ecommerce.price_service.domain.repository;

import com.ecommerce.price_service.domain.model.Price;

import java.time.LocalDateTime;
import java.util.List;

public interface PriceRepository {

    List<Price> findApplicablePrices(Long brandId, Long productId, LocalDateTime applicationDate);

}
