package com.ecommerce.price_service.infrastructure.adapter.in.rest;

import com.ecommerce.price_service.application.exception.InvalidPriceQueryException;
import com.ecommerce.price_service.domain.exception.PriceNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);
    private static final String INVALID_REQUEST = "Invalid request";

    @ExceptionHandler(PriceNotFoundException.class)
    public ProblemDetail handlePriceNotFound(PriceNotFoundException ex) {
        return problem(HttpStatus.NOT_FOUND, "Price not found", ex.getMessage());
    }

    @ExceptionHandler(InvalidPriceQueryException.class)
    public ProblemDetail handleInvalidQuery(InvalidPriceQueryException ex) {
        return problem(HttpStatus.BAD_REQUEST, INVALID_REQUEST, ex.getMessage());
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ProblemDetail handleMissingParameter(MissingServletRequestParameterException ex) {
        return problem(HttpStatus.BAD_REQUEST, INVALID_REQUEST,
                "Required parameter '%s' is missing".formatted(ex.getParameterName()));
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ProblemDetail handleTypeMismatch(MethodArgumentTypeMismatchException ex) {
        return problem(HttpStatus.BAD_REQUEST, INVALID_REQUEST,
                "Parameter '%s' has an invalid value: '%s'".formatted(ex.getName(), ex.getValue()));
    }

    @ExceptionHandler(NoResourceFoundException.class)
    public ProblemDetail handleNoResource(NoResourceFoundException ex) {
        return problem(HttpStatus.NOT_FOUND, "Resource not found",
                "No endpoint found for '%s'".formatted(ex.getResourcePath()));
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ProblemDetail handleMethodNotSupported(HttpRequestMethodNotSupportedException ex) {
        return problem(HttpStatus.METHOD_NOT_ALLOWED, "Method not allowed",
                "Method '%s' is not supported for this endpoint".formatted(ex.getMethod()));
    }

    @ExceptionHandler(Exception.class)
    public ProblemDetail handleUnexpected(Exception ex) {
        log.error("Unexpected error", ex);
        return problem(HttpStatus.INTERNAL_SERVER_ERROR, "Internal server error",
                "An unexpected error occurred");
    }

    private ProblemDetail problem(HttpStatus status, String title, String detail) {
        ProblemDetail problem = ProblemDetail.forStatusAndDetail(status, detail);
        problem.setTitle(title);
        return problem;
    }
}