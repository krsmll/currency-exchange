package me.krsmll.exchange.currency.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;

import com.github.tomakehurst.wiremock.client.WireMock;

import me.krsmll.exchange.currency.AbstractIntegrationTest;

import lombok.SneakyThrows;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class CurrencyControllerTest extends AbstractIntegrationTest {
    @Autowired
    private MockMvc mockMvc;

    @Test
    @SneakyThrows
    public void getCurrencies() {
        mockMvc.perform(get("/currencies"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.currencies", hasSize(3)))
                .andExpect(jsonPath("$.currencies[0].code", is("AUD")))
                .andExpect(jsonPath("$.currencies[0].name", is("Australian dollar")))
                .andExpect(jsonPath("$.currencies[1].code", is("EUR")))
                .andExpect(jsonPath("$.currencies[1].name", is("Euro")))
                .andExpect(jsonPath("$.currencies[2].code", is("USD")))
                .andExpect(jsonPath("$.currencies[2].name", is("US dollar")));
    }

    @Test
    @SneakyThrows
    public void getCurrencyRateHistory() {
        wireMockServer.stubFor(WireMock.get(urlPathEqualTo(LB_EXCHANGE_RATE_FOR_SPECIFIED_DATES_ENDPOINT))
                .withQueryParam("ccy", equalTo("USD"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "text/xml")
                        .withBodyFile("LbCurrencyRatesForDatesUsdResponse.xml")));
        wireMockServer.stubFor(WireMock.get(urlPathEqualTo(LB_EXCHANGE_RATE_FOR_SPECIFIED_DATES_ENDPOINT))
                .withQueryParam("ccy", equalTo("AUD"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "text/xml")
                        .withBodyFile("LbCurrencyRatesForDatesAudResponse.xml")));

        mockMvc.perform(get("/currencies/history?from=USD&to=AUD"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.from.code", is("USD")))
                .andExpect(jsonPath("$.from.name", is("US dollar")))
                .andExpect(jsonPath("$.to.code", is("AUD")))
                .andExpect(jsonPath("$.to.name", is("Australian dollar")))
                .andExpect(jsonPath("$.fromUnix", is(1715040000)))
                .andExpect(jsonPath("$.toUnix", is(1419984000)))
                .andExpect(jsonPath("$.history", hasSize(4)));
    }

    @Test
    @SneakyThrows
    public void convert() {
        mockMvc.perform(get("/currencies/exchange?from=USD&to=EUR&amount=100"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.from.code", is("USD")))
                .andExpect(jsonPath("$.from.name", is("US dollar")))
                .andExpect(jsonPath("$.to.code", is("EUR")))
                .andExpect(jsonPath("$.to.name", is("Euro")))
                .andExpect(jsonPath("$.rate", is(0.928850)))
                .andExpect(jsonPath("$.originalAmount", is(100)))
                .andExpect(jsonPath("$.convertedAmount", is(92.88)));
    }

    @Test
    @SneakyThrows
    public void convertWithInvalidCurrency() {
        mockMvc.perform(get("/currencies/exchange?from=USD&to=INVALID&amount=100"))
                .andExpect(status().isBadRequest());
    }

    @Test
    @SneakyThrows
    public void convertWithInvalidAmount() {
        mockMvc.perform(get("/currencies/exchange?from=USD&to=EUR&amount=-100")).andExpect(status().isBadRequest());
    }
}
