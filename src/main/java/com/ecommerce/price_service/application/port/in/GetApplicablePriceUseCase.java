package com.ecommerce.price_service.application.port.in;

import com.ecommerce.price_service.domain.model.Price;
/**
 * Input port: returns the price that applies to a product of a brand at a given date.
 *
 * @throws com.ecommerce.pricing.domain.exception.PriceNotFoundException if no price applies
 */
public interface GetApplicablePriceUseCase {

    Price getApplicablePrice(GetApplicablePriceQuery query);
}
