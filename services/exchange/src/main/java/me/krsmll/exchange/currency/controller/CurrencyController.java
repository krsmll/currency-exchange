package me.krsmll.exchange.currency.controller;

import lombok.RequiredArgsConstructor;
import me.krsmll.exchange.currency.controller.spec.CurrencyControllerSpec;
import me.krsmll.exchange.currency.dto.CurrencyConversionResultResponse;
import me.krsmll.exchange.currency.service.CurrencyService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/currency")
public class CurrencyController implements CurrencyControllerSpec {
    private final Optional<CurrencyService> currencyService;

    @Override
    @GetMapping("/exchange")
    public ResponseEntity<CurrencyConversionResultResponse> getLatestCurrencyExchangeRates(
            @RequestParam(name = "from") String fromCurrencyCode,
            @RequestParam(name = "to") String toCurrencyCode,
            @RequestParam(name = "amount") BigDecimal amount
    ) {
        CurrencyService service = currencyService.orElseThrow(() -> new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Currency provider is not configured"));
        return ResponseEntity.ok(service.convert(fromCurrencyCode, toCurrencyCode, amount));
    }
}
