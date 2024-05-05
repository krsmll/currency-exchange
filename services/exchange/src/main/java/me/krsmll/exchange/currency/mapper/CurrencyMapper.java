package me.krsmll.exchange.currency.mapper;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.util.Pair;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import me.krsmll.exchange.currency.dto.CurrencyConversionResultResponse;
import me.krsmll.exchange.currency.dto.CurrencyDto;
import me.krsmll.exchange.currency.dto.CurrencyListResponse;
import me.krsmll.exchange.currency.entity.CurrencyRateAgainstEuro;
import me.krsmll.libs.lb.dto.LbCurrencyDto;
import me.krsmll.libs.lb.dto.LbCurrencyExchangeRateDto;

@Component
public class CurrencyMapper {
    public CurrencyConversionResultResponse toCurrencyConversionResultResponse(
            CurrencyDto from,
            CurrencyDto to,
            BigDecimal rate,
            Integer minorUnits,
            BigDecimal originalAmount,
            BigDecimal convertedAmount) {
        return new CurrencyConversionResultResponse(from, to, rate, minorUnits, originalAmount, convertedAmount);
    }

    public CurrencyDto toCurrencyDto(CurrencyRateAgainstEuro currencyRateAgainstEuro) {
        return new CurrencyDto(currencyRateAgainstEuro.getCurrencyCode(), currencyRateAgainstEuro.getCurrencyName());
    }

    public CurrencyRateAgainstEuro currencyDataPairToEntity(Pair<LbCurrencyDto, LbCurrencyExchangeRateDto> pair) {
        LbCurrencyDto currency = pair.getFirst();
        LbCurrencyExchangeRateDto rate = pair.getSecond();
        String englishName = currency.getEnglishName()
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.INTERNAL_SERVER_ERROR,
                        String.format("English name for currency %s not found", currency.getCode())));

        return new CurrencyRateAgainstEuro(
                null, currency.getCode(), englishName, rate.getRate(), currency.getCurrencyMinorUnits(), null, null);
    }

    public CurrencyListResponse toCurrencyListResponse(List<CurrencyRateAgainstEuro> currencies) {
        return new CurrencyListResponse(
                currencies.stream().map(this::toCurrencyDto).collect(Collectors.toList()));
    }
}
