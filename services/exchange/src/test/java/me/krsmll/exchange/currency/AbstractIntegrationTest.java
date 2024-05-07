package me.krsmll.exchange.currency;

import me.krsmll.exchange.currency.entity.CurrencyRateAgainstEuro;
import me.krsmll.exchange.currency.repository.CurrencyRepository;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

import com.github.tomakehurst.wiremock.WireMockServer;

import java.math.BigDecimal;
import java.util.List;


@SpringBootTest
@DirtiesContext
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public abstract class AbstractIntegrationTest {
    public final WireMockServer wireMockServer = new WireMockServer();

    @Autowired
    private CurrencyRepository currencyRepository;

    @BeforeAll
    public void setup() {
        CurrencyRateAgainstEuro aud = new CurrencyRateAgainstEuro(null, "AUD", "Australian dollar", BigDecimal.valueOf(1.6297), 2, null, null);
        CurrencyRateAgainstEuro eur = new CurrencyRateAgainstEuro(null, "EUR", "Euro", BigDecimal.valueOf(1.0), 2, null, null);
        CurrencyRateAgainstEuro usd = new CurrencyRateAgainstEuro(null, "USD", "US dollar", BigDecimal.valueOf(1.0766), 2, null, null);
        currencyRepository.saveAll(List.of(aud, eur, usd));

        wireMockServer.start();
    }

    @AfterAll
    public void teardown() {
        wireMockServer.stop();
    }
}
