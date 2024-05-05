package me.krsmll.libs.lb.dto;

import java.util.List;
import javax.xml.bind.annotation.*;

import lombok.Setter;

@Setter
@XmlRootElement(name = "CcyTbl")
public class LbCurrencyDataDto {
    private List<LbCurrencyDto> currencies;

    @XmlElement(name = "CcyNtry")
    public List<LbCurrencyDto> getCurrencies() {
        return currencies;
    }
}
