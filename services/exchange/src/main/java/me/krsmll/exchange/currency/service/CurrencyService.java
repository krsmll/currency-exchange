package me.krsmll.exchange.currency.service;


import me.krsmll.exchange.currency.dto.CurrencyConversionResultResponse;

import java.math.BigDecimal;

public interface CurrencyService {
    CurrencyConversionResultResponse convert(String fromCurrencyCode, String toCurrencyCode, BigDecimal amount);
    void updateCurrencyRates();
}
