package me.krsmll.exchange.currency.service;

import java.math.BigDecimal;

import me.krsmll.exchange.currency.dto.CurrencyConversionResultResponse;
import me.krsmll.exchange.currency.dto.CurrencyListResponse;

public interface CurrencyService {
    CurrencyListResponse getCurrencies();

    CurrencyConversionResultResponse convert(String fromCurrencyCode, String toCurrencyCode, BigDecimal amount);

    void updateCurrencyRates();
}
