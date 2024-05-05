package me.krsmll.exchange.currency.service;

import java.math.BigDecimal;
import me.krsmll.exchange.currency.dto.CurrencyConversionResultResponse;

public interface CurrencyService {
    CurrencyConversionResultResponse convert(String fromCurrencyCode, String toCurrencyCode, BigDecimal amount);

    void updateCurrencyRates();
}
