package me.krsmll.libs.lb.dto;

import java.util.List;
import javax.xml.bind.annotation.*;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@NoArgsConstructor
@AllArgsConstructor
@XmlRootElement(name = "CcyTbl")
public class LbCurrencyDataDto {
    private List<LbCurrencyDto> currencies;

    @XmlElement(name = "CcyNtry")
    public List<LbCurrencyDto> getCurrencies() {
        return currencies;
    }
}
