package me.krsmll.exchange.currency.dto;

import java.math.BigDecimal;

import io.swagger.v3.oas.annotations.media.Schema;

public record CurrencyRateNodeDto(
        @Schema(description = "Unix timestamp of the date", example = "1612224000") Long dateUnixSeconds,
        @Schema(description = "Date in ISO 8601 format", example = "2021-02-02") String date,
        @Schema(description = "Exchange rate", example = "1.234567") BigDecimal rate) {}
