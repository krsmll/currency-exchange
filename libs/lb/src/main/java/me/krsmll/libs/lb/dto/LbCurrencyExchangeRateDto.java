package me.krsmll.libs.lb.dto;

import java.math.BigDecimal;
import javax.xml.bind.annotation.XmlElement;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@NoArgsConstructor
@AllArgsConstructor
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
