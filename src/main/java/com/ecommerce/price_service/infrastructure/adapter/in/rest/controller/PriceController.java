package com.ecommerce.price_service.infrastructure.adapter.in.rest.controller;

import com.ecommerce.price_service.application.port.in.GetApplicablePriceQuery;
import com.ecommerce.price_service.application.port.in.GetApplicablePriceUseCase;
import com.ecommerce.price_service.infrastructure.adapter.in.rest.dto.PriceResponse;
import com.ecommerce.price_service.infrastructure.adapter.in.rest.mapper.PriceRestMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.Positive;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/v1/prices")
@Tag(name = "Prices", description = "Price lookup")
public class PriceController {

    private final GetApplicablePriceUseCase getApplicablePriceUseCase;

    public PriceController(GetApplicablePriceUseCase getApplicablePriceUseCase) {
        this.getApplicablePriceUseCase = getApplicablePriceUseCase;
    }

    @GetMapping
    @Operation(summary = "Get the price that applies to a product of a brand at a given date")
    @ApiResponse(responseCode = "200", description = "Applicable price found")
    @ApiResponse(responseCode = "400", description = "Missing or invalid parameters")
    @ApiResponse(responseCode = "404", description = "No price applies at the given date")
    public PriceResponse getApplicablePrice(
            @Parameter(description = "Application date (ISO-8601)", example = "2020-06-14T16:00:00")
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime applicationDate,
            @Parameter(description = "Product identifier", example = "35455")
            @RequestParam @Positive Long productId,
            @Parameter(description = "Brand identifier", example = "1")
            @RequestParam @Positive Long brandId) {

        var query = new GetApplicablePriceQuery(brandId, productId, applicationDate);
        return PriceRestMapper.toResponse(getApplicablePriceUseCase.getApplicablePrice(query));
    }
}
