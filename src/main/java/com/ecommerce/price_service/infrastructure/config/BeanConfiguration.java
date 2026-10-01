package com.ecommerce.price_service.infrastructure.config;

import com.ecommerce.price_service.application.GetApplicablePriceService;
import com.ecommerce.price_service.application.port.in.GetApplicablePriceUseCase;
import com.ecommerce.price_service.domain.repository.PriceRepository;
import com.ecommerce.price_service.domain.service.ApplicablePriceResolver;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BeanConfiguration {

    @Bean
    public ApplicablePriceResolver applicablePriceResolver() {
        return new ApplicablePriceResolver();
    }

    @Bean
    public GetApplicablePriceUseCase getApplicablePriceUseCase(PriceRepository priceRepository,
                                                               ApplicablePriceResolver applicablePriceResolver) {
        return new GetApplicablePriceService(priceRepository, applicablePriceResolver);
    }
}
