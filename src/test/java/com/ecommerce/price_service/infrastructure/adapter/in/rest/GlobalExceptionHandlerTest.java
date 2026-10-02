package com.ecommerce.price_service.infrastructure.adapter.in.rest;

import com.ecommerce.price_service.application.exception.InvalidPriceQueryException;
import com.ecommerce.price_service.domain.exception.PriceNotFoundException;
import org.junit.jupiter.api.Test;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

class GlobalExceptionHandlerTest {

    private final GlobalExceptionHandler handler = new GlobalExceptionHandler();

    @Test
    void mapsPriceNotFoundToNotFound() {
        var ex = new PriceNotFoundException(1L, 35455L, LocalDateTime.of(2019, 1, 1, 0, 0));

        ProblemDetail problem = handler.handlePriceNotFound(ex);

        assertThat(problem.getStatus()).isEqualTo(404);
        assertThat(problem.getTitle()).isEqualTo("Price not found");
        assertThat(problem.getDetail()).contains("35455");
    }

    @Test
    void mapsInvalidQueryToBadRequest() {
        var ex = new InvalidPriceQueryException("brandId must be a positive number");

        ProblemDetail problem = handler.handleInvalidQuery(ex);

        assertThat(problem.getStatus()).isEqualTo(400);
        assertThat(problem.getTitle()).isEqualTo("Invalid request");
        assertThat(problem.getDetail()).isEqualTo("brandId must be a positive number");
    }

    @Test
    void mapsMissingParameterToBadRequest() {
        var ex = new MissingServletRequestParameterException("brandId", "Long");

        ProblemDetail problem = handler.handleMissingParameter(ex);

        assertThat(problem.getStatus()).isEqualTo(400);
        assertThat(problem.getDetail()).isEqualTo("Required parameter 'brandId' is missing");
    }

    @Test
    void mapsTypeMismatchToBadRequest() {
        var ex = new MethodArgumentTypeMismatchException(
                "14/06/2020", LocalDateTime.class, "applicationDate", null, null);

        ProblemDetail problem = handler.handleTypeMismatch(ex);

        assertThat(problem.getStatus()).isEqualTo(400);
        assertThat(problem.getDetail()).contains("applicationDate").contains("14/06/2020");
    }

    @Test
    void hidesDetailsOfUnexpectedErrors() {
        ProblemDetail problem = handler.handleUnexpected(new RuntimeException("database password leaked"));

        assertThat(problem.getStatus()).isEqualTo(500);
        assertThat(problem.getDetail()).doesNotContain("password");
    }
}