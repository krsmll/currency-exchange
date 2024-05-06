package me.krsmll.exchange.currency.dto;

import java.util.List;

public record CurrencyRateHistoryResponse(
        CurrencyDto from, CurrencyDto to, Long fromUnix, Long toUnix, List<CurrencyRateNodeDto> history) {}
