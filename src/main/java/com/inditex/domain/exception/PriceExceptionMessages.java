package com.inditex.domain.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum PriceExceptionMessages {

    PRICE_NOT_FOUND("Price not found");

    private String message;
}
