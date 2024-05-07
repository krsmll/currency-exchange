package me.krsmll.libs.lb.dto;

import java.util.List;
import java.util.Optional;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@NoArgsConstructor
@AllArgsConstructor
@XmlType(propOrder = {"code", "currencyNames", "currencyNumber", "currencyMinorUnits"})
public class LbCurrencyDto {
    private String code;
    private List<LbCurrencyNameData> currencyNames;
    private Integer currencyNumber;
    private Integer currencyMinorUnits;

    @XmlElement(name = "Ccy")
    public String getCode() {
        return code;
    }

    @XmlElement(name = "CcyNm")
    public List<LbCurrencyNameData> getCurrencyNames() {
        return currencyNames;
    }

    @XmlElement(name = "CcyNbr")
    public Integer getCurrencyNumber() {
        return currencyNumber;
    }

    @XmlElement(name = "CcyMnrUnts")
    public Integer getCurrencyMinorUnits() {
        return currencyMinorUnits;
    }

    public Optional<String> getEnglishName() {
        return getName("EN");
    }

    public Optional<String> getName(String langCode) {
        return currencyNames.stream()
                .filter(name -> langCode.equals(name.getLang()))
                .map(LbCurrencyNameData::getName)
                .findFirst();
    }
}
