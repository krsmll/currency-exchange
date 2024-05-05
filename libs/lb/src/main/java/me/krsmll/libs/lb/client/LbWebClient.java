package me.krsmll.libs.lb.client;

import org.springframework.web.reactive.function.client.WebClient;

import me.krsmll.libs.lb.dto.LbCurrencyDataDto;
import me.krsmll.libs.lb.dto.LbCurrencyExchangeDataDto;
import me.krsmll.libs.lb.util.XmlUtil;

public class LbWebClient {
    private static final String EXCHANGE_RATE_ENDPOINT = "/webservices/FxRates/FxRates.asmx/getCurrentFxRates";
    private static final String CURRENCY_ENDPOINT = "/webservices/FxRates/FxRates.asmx/getCurrencyList";

    private final WebClient webClient;

    public LbWebClient(WebClient webClient) {
        this.webClient = webClient;
    }

    public LbCurrencyExchangeDataDto getLatestExchangeRates(String type) {
        var str = this.webClient
                .get()
                .uri(uriBuilder -> uriBuilder
                        .path(EXCHANGE_RATE_ENDPOINT)
                        .queryParam("tp", type)
                        .build())
                .retrieve()
                .bodyToMono(String.class)
                .block();
        return XmlUtil.unmarshal(str, LbCurrencyExchangeDataDto.class);
    }

    public LbCurrencyDataDto getCurrencyData() {
        var str = this.webClient
                .get()
                .uri(CURRENCY_ENDPOINT)
                .retrieve()
                .bodyToMono(String.class)
                .block();
        return XmlUtil.unmarshal(str, LbCurrencyDataDto.class);
    }
}
