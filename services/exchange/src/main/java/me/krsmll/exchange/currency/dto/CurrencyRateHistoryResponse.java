package me.krsmll.exchange.currency.dto;

import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;

public record CurrencyRateHistoryResponse(
        @Schema(description = "From currency") CurrencyDto from,
        @Schema(description = "To currency") CurrencyDto to,
        @Schema(description = "Unix timestamp of the start of the period", example = "1612224000") Long fromUnix,
        @Schema(description = "Unix timestamp of the end of the period", example = "1612137600") Long toUnix,
        @Schema(description = "List of currency rate nodes") List<CurrencyRateNodeDto> history) {}
