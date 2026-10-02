package com.ecommerce.price_service.application.exception;

public class InvalidPriceQueryException extends RuntimeException {

    public InvalidPriceQueryException(String message) {
        super(message);
    }
}
