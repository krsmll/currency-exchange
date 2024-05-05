package me.krsmll.libs.lb.dto;

import java.util.List;
import javax.xml.bind.annotation.XmlElement;

import lombok.Setter;

@Setter
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
}
