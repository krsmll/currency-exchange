package me.krsmll.exchange.common.configuration;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.codec.xml.Jaxb2XmlDecoder;
import org.springframework.http.codec.xml.Jaxb2XmlEncoder;
import org.springframework.web.reactive.function.client.WebClient;

import me.krsmll.libs.lb.client.LbWebClient;

@Configuration
public class WebClientConfiguration {
    private final String lbUrl;

    public WebClientConfiguration(@Value("${currency.lb.url:#{null}}") Optional<String> lbUrl) {
        this.lbUrl = lbUrl.orElse(null);
    }

    @Bean
    public WebClient webClientForLbWebClient() {
        int size = 16 * 1024 * 1024;
        return WebClient.builder()
                .baseUrl(lbUrl)
                .codecs(configurer -> configurer.defaultCodecs().jaxb2Decoder(new Jaxb2XmlDecoder()))
                .codecs(configurer -> configurer.defaultCodecs().jaxb2Encoder(new Jaxb2XmlEncoder()))
                .codecs(configurer -> configurer.defaultCodecs().maxInMemorySize(size))
                .defaultHeader(HttpHeaders.ACCEPT, MediaType.APPLICATION_XML_VALUE, MediaType.TEXT_XML_VALUE)
                .build();
    }

    @Bean
    public LbWebClient lbWebClient(@Qualifier("webClientForLbWebClient") WebClient webClient) {
        return new LbWebClient(webClient);
    }
}
