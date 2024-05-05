package dto;

import java.util.List;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import lombok.Setter;

@Setter
@XmlRootElement(name = "FxRates")
public class LbCurrencyExchangeDataDto {
    private List<LbCurrencyExchangeRatesDto> exchangeRates;

    @XmlElement(name = "FxRate")
    public List<LbCurrencyExchangeRatesDto> getExchangeRates() {
        return exchangeRates;
    }
}
