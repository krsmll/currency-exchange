package me.krsmll.libs.lb.dto;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlValue;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@NoArgsConstructor
@AllArgsConstructor
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
