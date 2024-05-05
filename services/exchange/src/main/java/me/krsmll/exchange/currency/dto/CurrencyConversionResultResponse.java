package me.krsmll.exchange.currency.dto;

import java.math.BigDecimal;

public record CurrencyConversionResultResponse(
        CurrencyDto from,
        CurrencyDto to,
        BigDecimal rate,
        Integer minorUnits,
        BigDecimal originalAmount,
        BigDecimal convertedAmount
) {}
