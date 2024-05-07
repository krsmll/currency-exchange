package me.krsmll.libs.lb.dto;

import java.math.BigDecimal;
import java.util.List;
import javax.xml.bind.annotation.XmlElement;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LbCurrencyExchangeRatesDto {
    private String type;
    private String date;
    private List<LbCurrencyExchangeRateDto> rates;

    @XmlElement(name = "Tp")
    public String getType() {
        return type;
    }

    @XmlElement(name = "Dt")
    public String getDate() {
        return date;
    }

    @XmlElement(name = "CcyAmt")
    public List<LbCurrencyExchangeRateDto> getRates() {
        return rates;
    }

    public BigDecimal getCurrencyRate() {
        return this.getRates().stream()
                .filter(rate -> !rate.getCode().equals("EUR"))
                .findFirst()
                .map(LbCurrencyExchangeRateDto::getRate)
                .orElseThrow(
                        () -> new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Currency rate not found"));
    }
}
