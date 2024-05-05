package dto;

import lombok.Setter;

import javax.xml.bind.annotation.XmlElement;
import java.util.List;

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
