package me.krsmll.exchange.currency.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.TestPropertySource;

import me.krsmll.exchange.currency.dto.CurrencyConversionResultResponse;
import me.krsmll.exchange.currency.dto.CurrencyRateHistoryResponse;
import me.krsmll.exchange.currency.entity.CurrencyRateAgainstEuro;
import me.krsmll.exchange.currency.mapper.CurrencyMapper;
import me.krsmll.exchange.currency.repository.CurrencyRepository;
import me.krsmll.libs.lb.client.LbWebClient;
import me.krsmll.libs.lb.dto.LbCurrencyExchangeDataDto;
import me.krsmll.libs.lb.dto.LbCurrencyExchangeRateDto;
import me.krsmll.libs.lb.dto.LbCurrencyExchangeRatesDto;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@TestPropertySource(properties = "scheduling.enabled=false")
public class CurrencyServiceLbTest {
    @Mock
    private CurrencyRepository currencyRepository;

    @Mock
    private LbWebClient lbWebClient;

    @Spy
    private CurrencyMapper currencyMapper;

    @InjectMocks
    private CurrencyServiceLb currencyService;

    @Test
    public void getCurrencies_success() {
        mockCurrencyRepository();
        currencyService.getCurrencies();

        verify(currencyRepository, times(1)).findAll();
        verify(currencyMapper, times(1)).toCurrencyListResponse(currencyRepository.findAll());
    }

    @Test
    public void convert_success() {
        mockGetUsd();
        mockGetEur();
        CurrencyConversionResultResponse result = currencyService.convert("USD", "EUR", BigDecimal.valueOf(100));

        verify(currencyRepository, times(2)).findByCurrencyCodeIgnoreCase(any());
        verify(currencyMapper, times(1)).toCurrencyConversionResultResponse(any(), any(), any(), any(), any(), any());

        assertThat(result).isNotNull();
        assertThat(result.from().code()).isEqualTo("USD");
        assertThat(result.to().code()).isEqualTo("EUR");
        assertThat(result.originalAmount()).isEqualTo(BigDecimal.valueOf(100));
        assertThat(result.convertedAmount()).isEqualTo(BigDecimal.valueOf(92.59));
    }

    @Test
    public void getCurrencyRateHistory_success() {
        mockGetUsd();
        mockGetAud();
        mockLbHistoryForUsd();
        mockLbHistoryForAud();

        CurrencyRateHistoryResponse result = currencyService.getCurrencyRateHistory("USD", "AUD");

        verify(currencyRepository, times(2)).findByCurrencyCodeIgnoreCase(any());
        verify(currencyMapper, times(1)).toCurrencyRateHistoryResponse(any(), any(), any(), any(), any());
        verify(lbWebClient, times(2)).getExchangeRatesForSpecifiedDates(any(), any(), any(), any());

        assertThat(result).isNotNull();
        assertThat(result.from().code()).isEqualTo("USD");
        assertThat(result.to().code()).isEqualTo("AUD");
        assertThat(result.history()).hasSize(3);
    }

    private void mockCurrencyRepository() {
        LocalDateTime now = LocalDateTime.now();
        when(currencyRepository.findAll())
                .thenReturn(List.of(
                        new CurrencyRateAgainstEuro(1L, "USD", "US Dollar", BigDecimal.valueOf(1.08), 2, now, now),
                        new CurrencyRateAgainstEuro(2L, "EUR", "Euro", BigDecimal.valueOf(1), 2, now, now),
                        new CurrencyRateAgainstEuro(
                                3L, "AUD", "Australian Dollar", BigDecimal.valueOf(1.63), 2, now, now)));
    }

    private void mockGetUsd() {
        LocalDateTime now = LocalDateTime.now();
        when(currencyRepository.findByCurrencyCodeIgnoreCase(eq("USD")))
                .thenReturn(Optional.of(
                        new CurrencyRateAgainstEuro(1L, "USD", "US Dollar", BigDecimal.valueOf(1.08), 2, now, now)));
    }

    private void mockGetEur() {
        LocalDateTime now = LocalDateTime.now();
        when(currencyRepository.findByCurrencyCodeIgnoreCase(eq("EUR")))
                .thenReturn(Optional.of(
                        new CurrencyRateAgainstEuro(2L, "EUR", "Euro", BigDecimal.valueOf(1), 2, now, now)));
    }

    private void mockGetAud() {
        LocalDateTime now = LocalDateTime.now();
        when(currencyRepository.findByCurrencyCodeIgnoreCase(eq("AUD")))
                .thenReturn(Optional.of(new CurrencyRateAgainstEuro(
                        3L, "AUD", "Australian Dollar", BigDecimal.valueOf(1.63), 2, now, now)));
    }

    private void mockLbHistoryForUsd() {
        when(lbWebClient.getExchangeRatesForSpecifiedDates(
                        eq("USD"), eq("EU"), any(LocalDate.class), any(LocalDate.class)))
                .thenReturn(new LbCurrencyExchangeDataDto(List.of(
                        new LbCurrencyExchangeRatesDto(
                                "EU",
                                "2024-05-06",
                                List.of(
                                        new LbCurrencyExchangeRateDto("EUR", BigDecimal.valueOf(1.00)),
                                        new LbCurrencyExchangeRateDto("USD", BigDecimal.valueOf(1.08)))),
                        new LbCurrencyExchangeRatesDto(
                                "EU",
                                "2024-05-05",
                                List.of(
                                        new LbCurrencyExchangeRateDto("EUR", BigDecimal.valueOf(1.00)),
                                        new LbCurrencyExchangeRateDto("USD", BigDecimal.valueOf(1.15)))),
                        new LbCurrencyExchangeRatesDto(
                                "EU",
                                "2024-05-04",
                                List.of(
                                        new LbCurrencyExchangeRateDto("EUR", BigDecimal.valueOf(1.00)),
                                        new LbCurrencyExchangeRateDto("USD", BigDecimal.valueOf(1.1)))))));
    }

    private void mockLbHistoryForAud() {
        when(lbWebClient.getExchangeRatesForSpecifiedDates(
                        eq("AUD"), eq("EU"), any(LocalDate.class), any(LocalDate.class)))
                .thenReturn(new LbCurrencyExchangeDataDto(List.of(
                        new LbCurrencyExchangeRatesDto(
                                "EU",
                                "2024-05-06",
                                List.of(
                                        new LbCurrencyExchangeRateDto("EUR", BigDecimal.valueOf(1.00)),
                                        new LbCurrencyExchangeRateDto("AUD", BigDecimal.valueOf(1.63)))),
                        new LbCurrencyExchangeRatesDto(
                                "EU",
                                "2024-05-05",
                                List.of(
                                        new LbCurrencyExchangeRateDto("EUR", BigDecimal.valueOf(1.00)),
                                        new LbCurrencyExchangeRateDto("AUD", BigDecimal.valueOf(1.75)))),
                        new LbCurrencyExchangeRatesDto(
                                "EU",
                                "2024-05-04",
                                List.of(
                                        new LbCurrencyExchangeRateDto("EUR", BigDecimal.valueOf(1.00)),
                                        new LbCurrencyExchangeRateDto("AUD", BigDecimal.valueOf(1.59)))))));
    }
}
