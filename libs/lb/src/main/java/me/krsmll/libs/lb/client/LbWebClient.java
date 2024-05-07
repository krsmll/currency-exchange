package me.krsmll.libs.lb.client;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import org.springframework.web.reactive.function.client.WebClient;

import me.krsmll.libs.common.client.WebClientExceptionHandler;
import me.krsmll.libs.lb.dto.LbCurrencyDataDto;
import me.krsmll.libs.lb.dto.LbCurrencyExchangeDataDto;
import me.krsmll.libs.lb.util.XmlUtil;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class LbWebClient {
    private final String EXCHANGE_RATE_ENDPOINT = "/webservices/FxRates/FxRates.asmx/getCurrentFxRates";
    private final String EXCHANGE_RATE_FOR_SPECIFIED_DATES_ENDPOINT =
            "/webservices/FxRates/FxRates.asmx/getFxRatesForCurrency";
    private static final String CURRENCY_ENDPOINT = "/webservices/FxRates/FxRates.asmx/getCurrencyList";

    private final WebClient webClient;

    public LbCurrencyExchangeDataDto getLatestExchangeRates(String type) {
        String str = this.webClient
                .get()
                .uri(uriBuilder -> uriBuilder
                        .path(EXCHANGE_RATE_ENDPOINT)
                        .queryParam("tp", type)
                        .build())
                .retrieve()
                .bodyToMono(String.class)
                .doOnError(WebClientExceptionHandler::handleWebClientError)
                .block();
        return XmlUtil.unmarshal(str, LbCurrencyExchangeDataDto.class);
    }

    public LbCurrencyExchangeDataDto getExchangeRatesForSpecifiedDates(
            String currencyCode, String type, LocalDate fromDate, LocalDate toDate) {
        String formattedFromDate = fromDate.format(DateTimeFormatter.ISO_DATE);
        String formattedToDate = toDate.format(DateTimeFormatter.ISO_DATE);
        String str = this.webClient
                .get()
                .uri(uriBuilder -> uriBuilder
                        .path(EXCHANGE_RATE_FOR_SPECIFIED_DATES_ENDPOINT)
                        .queryParam("tp", type)
                        .queryParam("ccy", currencyCode)
                        .queryParam("dtFrom", formattedFromDate)
                        .queryParam("dtTo", formattedToDate)
                        .build())
                .retrieve()
                .bodyToMono(String.class)
                .doOnError(WebClientExceptionHandler::handleWebClientError)
                .block();

        return XmlUtil.unmarshal(str, LbCurrencyExchangeDataDto.class);
    }

    public LbCurrencyDataDto getCurrencyData() {
        String str = this.webClient
                .get()
                .uri(CURRENCY_ENDPOINT)
                .retrieve()
                .bodyToMono(String.class)
                .doOnError(WebClientExceptionHandler::handleWebClientError)
                .block();
        return XmlUtil.unmarshal(str, LbCurrencyDataDto.class);
    }
}
