package me.krsmll.exchange.currency.dto;

import io.swagger.v3.oas.annotations.media.Schema;

public record CurrencyDto(
        @Schema(description = "Currency code", example = "USD") String code,
        @Schema(description = "Currency name", example = "US dollar") String name) {}
