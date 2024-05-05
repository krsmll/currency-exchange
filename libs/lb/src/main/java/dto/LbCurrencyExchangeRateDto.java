package dto;

import java.math.BigDecimal;
import javax.xml.bind.annotation.XmlElement;
import lombok.Data;

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
