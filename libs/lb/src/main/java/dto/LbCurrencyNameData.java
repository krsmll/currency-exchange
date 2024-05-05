package dto;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlValue;
import lombok.Setter;

@Setter
public class LbCurrencyNameData {
    private String lang;
    private String name;

    @XmlAttribute(name = "lang")
    public String getLang() {
        return lang;
    }

    @XmlValue
    public String getName() {
        return name;
    }
}
