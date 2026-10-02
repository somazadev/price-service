package com.ecommerce.price_service.infrastructure.adapter.out;

import com.ecommerce.price_service.domain.model.Price;
import com.ecommerce.price_service.domain.repository.PriceRepository;
import com.ecommerce.price_service.infrastructure.adapter.out.mapper.PriceEntityMapper;
import com.ecommerce.price_service.infrastructure.adapter.out.repository.SpringDataPriceRepository;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Component
public class PricePersistenceAdapter implements PriceRepository {

    private final SpringDataPriceRepository repository;

    public PricePersistenceAdapter(SpringDataPriceRepository repository) {
        this.repository = repository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Price> findApplicablePrices(Long brandId, Long productId, LocalDateTime applicationDate) {
        return repository.findApplicable(brandId, productId, applicationDate).stream().map(PriceEntityMapper::toDomain).toList();
    }
}