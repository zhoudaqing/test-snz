
package org.example.sendclaiminfotogvs;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for ZSPJH_INPUT_MONTHLY complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="ZSPJH_INPUT_MONTHLY">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="ZDATE" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ZSPJH_INPUT_MONTHLY", propOrder = {
    "zdate"
})
public class ZSPJHINPUTMONTHLY {

    @XmlElement(name = "ZDATE", required = true)
    protected String zdate;

    /**
     * Gets the value of the zdate property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getZDATE() {
        return zdate;
    }

    /**
     * Sets the value of the zdate property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setZDATE(String value) {
        this.zdate = value;
    }

}
