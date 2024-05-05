package dto;

import lombok.Setter;

import javax.xml.bind.annotation.*;
import java.util.List;

@Setter
@XmlRootElement(name = "CcyTbl")
public class LbCurrencyDataDto {
    private List<LbCurrencyDto> currencies;

    @XmlElement(name = "CcyNtry")
    public List<LbCurrencyDto> getCurrencies() {
        return currencies;
    }
}
