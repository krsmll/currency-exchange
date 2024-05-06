package me.krsmll.exchange.currency.controller;

import java.math.BigDecimal;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import me.krsmll.exchange.currency.controller.spec.CurrencyControllerSpec;
import me.krsmll.exchange.currency.dto.CurrencyConversionResultResponse;
import me.krsmll.exchange.currency.dto.CurrencyListResponse;
import me.krsmll.exchange.currency.dto.CurrencyRateHistoryResponse;
import me.krsmll.exchange.currency.service.CurrencyService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/currencies")
public class CurrencyController implements CurrencyControllerSpec {
    private final Optional<CurrencyService> currencyService;

    @Override
    @CrossOrigin
    @GetMapping
    public ResponseEntity<CurrencyListResponse> getCurrencies() {
        CurrencyService service = currencyService.orElseThrow(() ->
                new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Currency provider is not configured"));
        return ResponseEntity.ok(service.getCurrencies());
    }

    @Override
    @CrossOrigin
    @GetMapping("/history")
    public ResponseEntity<CurrencyRateHistoryResponse> getCurrencyRateHistory(
            @RequestParam(name = "from") String fromCurrencyCode, @RequestParam(name = "to") String toCurrencyCode) {
        CurrencyService service = currencyService.orElseThrow(() ->
                new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Currency provider is not configured"));
        return ResponseEntity.ok(service.getCurrencyRateHistory(fromCurrencyCode, toCurrencyCode));
    }

    @Override
    @CrossOrigin
    @GetMapping("/exchange")
    public ResponseEntity<CurrencyConversionResultResponse> convert(
            @RequestParam(name = "from") String fromCurrencyCode,
            @RequestParam(name = "to") String toCurrencyCode,
            @RequestParam(name = "amount") BigDecimal amount) {
        CurrencyService service = currencyService.orElseThrow(() ->
                new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Currency provider is not configured"));
        return ResponseEntity.ok(service.convert(fromCurrencyCode, toCurrencyCode, amount));
    }
}
