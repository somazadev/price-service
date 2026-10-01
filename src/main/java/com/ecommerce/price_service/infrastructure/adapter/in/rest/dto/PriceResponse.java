package com.ecommerce.price_service.infrastructure.adapter.in.rest.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Schema(description = "Price that applies to a product of a brand at the requested date")

public record PriceResponse(
        @Schema(example = "35455") Long productId,
        @Schema(example = "1") Long brandId,
        @Schema(description = "Price list (tariff) applied", example = "2") Integer priceList,
        @Schema(example = "2020-06-14T15:00:00")
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
        LocalDateTime startDate,
        @Schema(example = "2020-06-14T18:30:00")
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
        LocalDateTime endDate,
        @Schema(description = "Final price", example = "25.45") BigDecimal price,
        @Schema(description = "ISO 4217 currency code", example = "EUR") String currency
) {
}
