package dto;

import lombok.Setter;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@Setter
@XmlRootElement(name = "FxRates")
public class LbCurrencyExchangeDataDto {
        private List<LbCurrencyExchangeRatesDto> exchangeRates;

        @XmlElement(name = "FxRate")
        public List<LbCurrencyExchangeRatesDto> getExchangeRates() {
                return exchangeRates;
        }
}
