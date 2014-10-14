
package org.example.receiveclaiminfofromgvs;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for ZSPJH_INPUT2 complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="ZSPJH_INPUT2">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="Z_RETSTA" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="LIFNR" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="WERKS" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="MATNR" type="{http://www.w3.org/2001/XMLSchema}string"/>
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
@XmlType(name = "ZSPJH_INPUT2", propOrder = {
    "zretsta",
    "lifnr",
    "werks",
    "matnr",
    "zdate"
})
public class ZSPJHINPUT2 {

    @XmlElement(name = "Z_RETSTA", required = true)
    protected String zretsta;
    @XmlElement(name = "LIFNR", required = true)
    protected String lifnr;
    @XmlElement(name = "WERKS", required = true)
    protected String werks;
    @XmlElement(name = "MATNR", required = true)
    protected String matnr;
    @XmlElement(name = "ZDATE", required = true)
    protected String zdate;

    /**
     * Gets the value of the zretsta property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getZRETSTA() {
        return zretsta;
    }

    /**
     * Sets the value of the zretsta property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setZRETSTA(String value) {
        this.zretsta = value;
    }

    /**
     * Gets the value of the lifnr property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getLIFNR() {
        return lifnr;
    }

    /**
     * Sets the value of the lifnr property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setLIFNR(String value) {
        this.lifnr = value;
    }

    /**
     * Gets the value of the werks property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getWERKS() {
        return werks;
    }

    /**
     * Sets the value of the werks property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setWERKS(String value) {
        this.werks = value;
    }

    /**
     * Gets the value of the matnr property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMATNR() {
        return matnr;
    }

    /**
     * Sets the value of the matnr property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMATNR(String value) {
        this.matnr = value;
    }

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
