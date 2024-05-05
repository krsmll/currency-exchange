package me.krsmll.exchange.currency.controller.spec;

import me.krsmll.exchange.currency.dto.CurrencyConversionResultResponse;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;

public interface CurrencyControllerSpec {
    ResponseEntity<CurrencyConversionResultResponse> getLatestCurrencyExchangeRates(String fromCurrencyCode, String toCurrencyCode, BigDecimal amount);
}
