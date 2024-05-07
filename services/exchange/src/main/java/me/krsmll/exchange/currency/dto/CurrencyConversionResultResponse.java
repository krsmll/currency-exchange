package me.krsmll.exchange.currency.dto;

import java.math.BigDecimal;

import io.swagger.v3.oas.annotations.media.Schema;

public record CurrencyConversionResultResponse(
        @Schema(description = "From currency") CurrencyDto from,
        @Schema(description = "To currency") CurrencyDto to,
        @Schema(description = "Exchange rate", example = "1.234567") BigDecimal rate,
        @Schema(description = "Amount of currency minor units", example = "2") Integer minorUnits,
        @Schema(description = "Original amount", example = "100.00") BigDecimal originalAmount,
        @Schema(description = "Converted amount", example = "123.46") BigDecimal convertedAmount) {}
