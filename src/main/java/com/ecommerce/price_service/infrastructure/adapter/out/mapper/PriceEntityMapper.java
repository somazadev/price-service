package com.ecommerce.price_service.infrastructure.adapter.out.mapper;

import com.ecommerce.price_service.domain.model.Price;
import com.ecommerce.price_service.infrastructure.adapter.out.entity.PriceEntity;

import java.util.Currency;

public class PriceEntityMapper {

    private PriceEntityMapper() {
    }

    public static Price toDomain(PriceEntity entity) {
        return new Price(
                entity.getBrandId(),
                entity.getProductId(),
                entity.getPriceList(),
                entity.getPriority(),
                entity.getStartDate(),
                entity.getEndDate(),
                entity.getPrice(),
                Currency.getInstance(entity.getCurrency().trim())
        );
    }
}
