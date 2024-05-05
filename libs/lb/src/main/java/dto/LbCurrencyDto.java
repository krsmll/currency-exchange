package dto;

import lombok.Setter;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import java.util.List;
import java.util.Optional;

@Setter
@XmlType(propOrder = {"code", "currencyNames", "currencyNumber", "currencyMinorUnits"})
public class LbCurrencyDto {
    private String code;
    private List<LbCurrencyNameData> currencyNames;
    private String currencyNumber;
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
    public String getCurrencyNumber() {
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
