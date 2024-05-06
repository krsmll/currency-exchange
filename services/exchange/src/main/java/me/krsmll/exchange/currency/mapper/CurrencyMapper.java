package me.krsmll.exchange.currency.mapper;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.util.Pair;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import me.krsmll.exchange.currency.dto.*;
import me.krsmll.exchange.currency.entity.CurrencyRateAgainstEuro;
import me.krsmll.libs.lb.dto.LbCurrencyDto;
import me.krsmll.libs.lb.dto.LbCurrencyExchangeRateDto;
import me.krsmll.libs.lb.dto.LbCurrencyExchangeRatesDto;

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
                        String.format("English name for toCurrency %s not found", currency.getCode())));

        return new CurrencyRateAgainstEuro(
                null, currency.getCode(), englishName, rate.getRate(), currency.getCurrencyMinorUnits(), null, null);
    }

    public CurrencyListResponse toCurrencyListResponse(List<CurrencyRateAgainstEuro> currencies) {
        return new CurrencyListResponse(
                currencies.stream().map(this::toCurrencyDto).collect(Collectors.toList()));
    }

    public CurrencyRateHistoryResponse toCurrencyRateHistoryResponse(
            CurrencyRateAgainstEuro fromCurrency,
            CurrencyRateAgainstEuro toCurrency,
            LocalDate currentDate,
            LocalDate toDate,
            List<CurrencyRateNodeDto> history) {
        return new CurrencyRateHistoryResponse(
                toCurrencyDto(fromCurrency),
                toCurrencyDto(toCurrency),
                currentDate.toEpochSecond(LocalTime.MIDNIGHT, ZoneOffset.UTC),
                toDate.toEpochSecond(LocalTime.MIDNIGHT, ZoneOffset.UTC),
                history);
    }

    public List<CurrencyRateNodeDto> toCurrencyRateNodeDtoList(
            List<LbCurrencyExchangeRatesDto> lbCurrencyExchangeRatesDtos) {
        return lbCurrencyExchangeRatesDtos.stream()
                .map(rate -> new CurrencyRateNodeDto(
                        LocalDate.parse(rate.getDate()).toEpochSecond(LocalTime.MIDNIGHT, ZoneOffset.UTC),
                        rate.getDate(),
                        rate.getCurrencyRate()))
                .collect(Collectors.toList());
    }
}
