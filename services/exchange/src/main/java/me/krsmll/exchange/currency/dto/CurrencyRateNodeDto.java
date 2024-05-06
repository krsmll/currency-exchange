package me.krsmll.exchange.currency.dto;

import java.math.BigDecimal;

public record CurrencyRateNodeDto(Long dateUnixSeconds, String date, BigDecimal rate) {}
