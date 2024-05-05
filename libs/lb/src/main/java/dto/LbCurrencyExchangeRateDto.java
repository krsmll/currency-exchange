package dto;

import lombok.Data;

import javax.xml.bind.annotation.XmlElement;
import java.math.BigDecimal;

@Data
public class LbCurrencyExchangeRateDto {
    private String code;
    private BigDecimal rate;

    @XmlElement(name = "Ccy")
    public String getCode() {
        return code;
    }

    @XmlElement(name = "Amt")
    public BigDecimal getRate() {
        return rate;
    }
}
