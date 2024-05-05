package me.krsmll.exchange.currency.controller.spec;

import java.math.BigDecimal;
import me.krsmll.exchange.currency.dto.CurrencyConversionResultResponse;
import org.springframework.http.ResponseEntity;

public interface CurrencyControllerSpec {
    ResponseEntity<CurrencyConversionResultResponse> getLatestCurrencyExchangeRates(
            String fromCurrencyCode, String toCurrencyCode, BigDecimal amount);
}
