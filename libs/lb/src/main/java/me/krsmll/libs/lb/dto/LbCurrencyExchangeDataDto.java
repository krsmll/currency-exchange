package me.krsmll.libs.lb.dto;

import java.util.List;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@NoArgsConstructor
@AllArgsConstructor
@XmlRootElement(name = "FxRates")
public class LbCurrencyExchangeDataDto {
    private List<LbCurrencyExchangeRatesDto> exchangeRates;

    @XmlElement(name = "FxRate")
    public List<LbCurrencyExchangeRatesDto> getExchangeRates() {
        return exchangeRates;
    }
}
