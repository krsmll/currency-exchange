package me.krsmll.exchange.currency.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.*;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.data.util.Pair;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.server.ResponseStatusException;

import me.krsmll.exchange.currency.dto.CurrencyConversionResultResponse;
import me.krsmll.exchange.currency.dto.CurrencyListResponse;
import me.krsmll.exchange.currency.dto.CurrencyRateHistoryResponse;
import me.krsmll.exchange.currency.dto.CurrencyRateNodeDto;
import me.krsmll.exchange.currency.entity.CurrencyRateAgainstEuro;
import me.krsmll.exchange.currency.mapper.CurrencyMapper;
import me.krsmll.exchange.currency.repository.CurrencyRepository;
import me.krsmll.libs.lb.client.LbWebClient;
import me.krsmll.libs.lb.dto.LbCurrencyDto;
import me.krsmll.libs.lb.dto.LbCurrencyExchangeRateDto;
import me.krsmll.libs.lb.dto.LbCurrencyExchangeRatesDto;

@Service
@ConditionalOnProperty(value = "currency.provider", havingValue = "lb")
public class CurrencyServiceLb implements CurrencyService {
    private static final String EXCHANGE_RATE_TYPE = "EU";
    private static final String EURO_CURRENCY_CODE = "EUR";
    private static final LocalDate MAX_DATE = LocalDate.of(2014, 12, 31);
    private static final int MAX_SOURCE_PRECISION = 6;

    private final LbWebClient lbWebClient;
    private final CurrencyRepository currencyRepository;
    private final CurrencyMapper currencyMapper;

    public CurrencyServiceLb(
            @Qualifier("LbWebClient") WebClient lbWebClient,
            CurrencyRepository currencyRepository,
            CurrencyMapper currencyMapper) {
        this.lbWebClient = new LbWebClient(lbWebClient);
        this.currencyRepository = currencyRepository;
        this.currencyMapper = currencyMapper;
    }

    @Override
    public CurrencyListResponse getCurrencies() {
        List<CurrencyRateAgainstEuro> currencies = currencyRepository.findAll();
        return currencyMapper.toCurrencyListResponse(currencies);
    }

    @Override
    public CurrencyRateHistoryResponse getCurrencyRateHistory(String fromCurrencyCode, String toCurrencyCode) {
        CurrencyRateAgainstEuro fromCurrency = findCurrencyByCode(fromCurrencyCode);
        CurrencyRateAgainstEuro toCurrency = findCurrencyByCode(toCurrencyCode);
        LocalDate currentDate = LocalDate.now();
        if (EURO_CURRENCY_CODE.equalsIgnoreCase(toCurrencyCode)) {
            return currencyMapper.toCurrencyRateHistoryResponse(
                    fromCurrency,
                    toCurrency,
                    currentDate,
                    MAX_DATE,
                    handleToEuroHistory(getExchangeRatesForSpecifiedDates(fromCurrencyCode, currentDate)));
        }

        List<LbCurrencyExchangeRatesDto> toCurrencyExchangeRateHistory =
                getExchangeRatesForSpecifiedDates(toCurrencyCode, currentDate);
        if (EURO_CURRENCY_CODE.equalsIgnoreCase(fromCurrencyCode)) {
            return currencyMapper.toCurrencyRateHistoryResponse(
                    fromCurrency,
                    toCurrency,
                    currentDate,
                    MAX_DATE,
                    currencyMapper.toCurrencyRateNodeDtoList(toCurrencyExchangeRateHistory));
        }

        List<LbCurrencyExchangeRatesDto> fromCurrencyExchangeRateHistory =
                getExchangeRatesForSpecifiedDates(fromCurrencyCode, currentDate);
        List<CurrencyRateNodeDto> adjustedRates =
                adjustRatesForDifferentCurrency(fromCurrencyExchangeRateHistory, toCurrencyExchangeRateHistory);
        return currencyMapper.toCurrencyRateHistoryResponse(
                fromCurrency, toCurrency, currentDate, MAX_DATE, adjustedRates);
    }

    @Override
    public CurrencyConversionResultResponse convert(String fromCurrencyCode, String toCurrencyCode, BigDecimal amount) {
        CurrencyRateAgainstEuro fromCurrency = findCurrencyByCode(fromCurrencyCode);
        CurrencyRateAgainstEuro toCurrency = findCurrencyByCode(toCurrencyCode);

        BigDecimal conversionRate = calculateConversionRate(fromCurrency, toCurrency);
        return currencyMapper.toCurrencyConversionResultResponse(
                currencyMapper.toCurrencyDto(fromCurrency),
                currencyMapper.toCurrencyDto(toCurrency),
                conversionRate,
                toCurrency.getMinorUnits(),
                amount,
                amount.multiply(conversionRate).setScale(toCurrency.getMinorUnits(), RoundingMode.HALF_EVEN));
    }

    @Override
    public void updateCurrencyRates() {
        List<Pair<LbCurrencyDto, LbCurrencyExchangeRateDto>> pairedData = getCurrenciesWithRates();
        List<CurrencyRateAgainstEuro> dbRates = currencyRepository.findAll();
        if (dbRates.isEmpty()) {
            List<CurrencyRateAgainstEuro> newRates = pairedData.stream()
                    .map(currencyMapper::currencyDataPairToEntity)
                    .toList();
            currencyRepository.saveAll(newRates);
        } else {
            updateDatabaseRates(pairedData, dbRates);
        }
    }

    private List<CurrencyRateNodeDto> handleToEuroHistory(
            List<LbCurrencyExchangeRatesDto> toCurrencyExchangeRateHistory) {
        List<CurrencyRateNodeDto> newRates = new ArrayList<>();
        toCurrencyExchangeRateHistory.forEach(rate -> {
            newRates.add(new CurrencyRateNodeDto(
                    LocalDate.parse(rate.getDate()).toEpochSecond(LocalTime.MIDNIGHT, ZoneOffset.UTC),
                    rate.getDate(),
                    BigDecimal.valueOf(1L)
                            .divide(rate.getCurrencyRate(), MAX_SOURCE_PRECISION, RoundingMode.HALF_EVEN)));
        });

        return newRates;
    }

    private List<CurrencyRateNodeDto> adjustRatesForDifferentCurrency(
            List<LbCurrencyExchangeRatesDto> fromCurrencyExchangeRateHistory,
            List<LbCurrencyExchangeRatesDto> toCurrencyExchangeRateHistory) {
        List<CurrencyRateNodeDto> adjustedRates = new ArrayList<>();
        Stack<LbCurrencyExchangeRatesDto> fromStack = new Stack<>();
        Stack<LbCurrencyExchangeRatesDto> toStack = new Stack<>();
        fromStack.addAll(fromCurrencyExchangeRateHistory);
        toStack.addAll(toCurrencyExchangeRateHistory);

        LbCurrencyExchangeRatesDto fromRate = fromStack.pop();
        LbCurrencyExchangeRatesDto toRate = toStack.pop();
        DateTimeFormatter parser = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        while (!fromStack.isEmpty() && !toStack.isEmpty()) {
            Long fromDate = LocalDate.parse(fromRate.getDate()).toEpochSecond(LocalTime.MIDNIGHT, ZoneOffset.UTC);
            Long toDate = LocalDate.parse(toRate.getDate(), parser).toEpochSecond(LocalTime.MIDNIGHT, ZoneOffset.UTC);
            if (fromDate.equals(toDate)) {
                CurrencyRateNodeDto newToRate = new CurrencyRateNodeDto(
                        fromDate,
                        fromRate.getDate(),
                        calculateConversionRate(fromRate.getCurrencyRate(), toRate.getCurrencyRate()));

                adjustedRates.add(newToRate);
                fromRate = fromStack.pop();
                toRate = toStack.pop();
            } else if (fromDate.compareTo(toDate) > 0) {
                fromStack.pop();
                if (!fromStack.isEmpty()) {
                    fromRate = fromStack.pop();
                }
            } else {
                toStack.pop();
                if (!toStack.isEmpty()) {
                    toRate = toStack.pop();
                }
            }
        }

        return adjustedRates.stream()
                .sorted(Comparator.comparing(CurrencyRateNodeDto::dateUnixSeconds))
                .toList();
    }

    private List<LbCurrencyExchangeRatesDto> getExchangeRatesForSpecifiedDates(
            String currencyCode, LocalDate currentDate) {
        return lbWebClient
                .getExchangeRatesForSpecifiedDates(currencyCode, EXCHANGE_RATE_TYPE, currentDate, MAX_DATE)
                .getExchangeRates()
                .stream()
                .sorted(Comparator.comparing(LbCurrencyExchangeRatesDto::getDate))
                .toList();
    }

    private CurrencyRateAgainstEuro findCurrencyByCode(String currencyCode) {
        return currencyRepository
                .findByCurrencyCodeIgnoreCase(currencyCode)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.BAD_REQUEST, String.format("Currency with code %s not found", currencyCode)));
    }

    private BigDecimal calculateConversionRate(
            CurrencyRateAgainstEuro fromCurrency, CurrencyRateAgainstEuro toCurrency) {
        return toCurrency.getRate().divide(fromCurrency.getRate(), MAX_SOURCE_PRECISION, RoundingMode.HALF_EVEN);
    }

    private BigDecimal calculateConversionRate(BigDecimal fromRate, BigDecimal toRate) {
        return toRate.divide(fromRate, MAX_SOURCE_PRECISION, RoundingMode.HALF_EVEN);
    }

    private List<Pair<LbCurrencyDto, LbCurrencyExchangeRateDto>> getCurrenciesWithRates() {
        List<LbCurrencyDto> currencies = lbWebClient.getCurrencyData().getCurrencies();
        List<LbCurrencyExchangeRateDto> rates =
                lbWebClient.getLatestExchangeRates(EXCHANGE_RATE_TYPE).getExchangeRates().stream()
                        .map(LbCurrencyExchangeRatesDto::getRates)
                        .flatMap(List::stream)
                        .distinct()
                        .toList();

        return currencies.stream()
                .map(currency -> pairCurrencyWithMatchingRate(currency, rates))
                .filter(Objects::nonNull)
                .toList();
    }

    private Pair<LbCurrencyDto, LbCurrencyExchangeRateDto> pairCurrencyWithMatchingRate(
            LbCurrencyDto currency, List<LbCurrencyExchangeRateDto> rates) {
        return rates.stream()
                .filter(rate -> rate.getCode().equals(currency.getCode()))
                .findFirst()
                .map(rate -> Pair.of(currency, rate))
                .orElse(null);
    }

    private void updateDatabaseRates(
            List<Pair<LbCurrencyDto, LbCurrencyExchangeRateDto>> pairedData, List<CurrencyRateAgainstEuro> dbRates) {
        pairedData.forEach(pair -> {
            LbCurrencyDto currency = pair.getFirst();
            CurrencyRateAgainstEuro dbRate = dbRates.stream()
                    .filter(rate -> rate.getCurrencyCode().equals(currency.getCode()))
                    .findFirst()
                    .orElseThrow();
            dbRate.setRate(pair.getSecond().getRate());
        });

        currencyRepository.saveAll(dbRates);
    }
}
