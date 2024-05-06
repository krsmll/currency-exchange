package me.krsmll.exchange.currency.service;

import java.math.BigDecimal;

import me.krsmll.exchange.currency.dto.CurrencyConversionResultResponse;
import me.krsmll.exchange.currency.dto.CurrencyListResponse;
import me.krsmll.exchange.currency.dto.CurrencyRateHistoryResponse;

public interface CurrencyService {
    CurrencyListResponse getCurrencies();

    CurrencyRateHistoryResponse getCurrencyRateHistory(String fromCurrencyCode, String toCurrencyCode);

    CurrencyConversionResultResponse convert(String fromCurrencyCode, String toCurrencyCode, BigDecimal amount);

    void updateCurrencyRates();
}
