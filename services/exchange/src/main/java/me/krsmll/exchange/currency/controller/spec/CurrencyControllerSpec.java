package me.krsmll.exchange.currency.controller.spec;

import java.math.BigDecimal;

import org.springframework.http.ResponseEntity;

import me.krsmll.exchange.currency.dto.CurrencyConversionResultResponse;
import me.krsmll.exchange.currency.dto.CurrencyListResponse;

public interface CurrencyControllerSpec {
    ResponseEntity<CurrencyListResponse> getCurrencies();

    ResponseEntity<CurrencyConversionResultResponse> convert(
            String fromCurrencyCode, String toCurrencyCode, BigDecimal amount);
}
