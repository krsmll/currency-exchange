package me.krsmll.exchange.currency;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;

import static com.github.tomakehurst.wiremock.client.WireMock.*;

@SpringBootTest
@DirtiesContext
@AutoConfigureMockMvc
public abstract class AbstractIntegrationTest {
    protected static final String LB_CURRENCY_DATA_ENDPOINT = "/webservices/FxRates/FxRates.asmx/getCurrencyList";
    protected static final String LB_EXCHANGE_RATE_ENDPOINT = "/webservices/FxRates/FxRates.asmx/getCurrentFxRates";
    protected static final String LB_EXCHANGE_RATE_FOR_SPECIFIED_DATES_ENDPOINT =
            "/webservices/FxRates/FxRates.asmx/getFxRatesForCurrency";
    public static final WireMockServer wireMockServer = new WireMockServer();

    @BeforeAll
    public static void setup() {
        wireMockServer.stubFor(WireMock.get(urlPathEqualTo(LB_CURRENCY_DATA_ENDPOINT))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "text/xml")
                        .withBodyFile("LbCurrencyDataResponse.xml")));
        wireMockServer.stubFor(WireMock.get(urlPathEqualTo(LB_EXCHANGE_RATE_ENDPOINT))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "text/xml")
                        .withBodyFile("LbCurrencyLatestRatesResponse.xml")));
        wireMockServer.start();
    }

    @AfterAll
    public static void teardown() {
        wireMockServer.stop();
    }
}
