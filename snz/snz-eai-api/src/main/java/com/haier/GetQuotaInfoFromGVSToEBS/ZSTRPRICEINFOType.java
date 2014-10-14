
package com.haier.GetQuotaInfoFromGVSToEBS;

import java.math.BigDecimal;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for ZSTR_PRICE_INFOType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="ZSTR_PRICE_INFOType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="MATNR" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="MAKTX" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="MATKL" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="LIFNR" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="NAME1" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="EKORG" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="ESOKZ" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="KBETR" type="{http://www.w3.org/2001/XMLSchema}decimal"/>
 *         &lt;element name="KPEIN" type="{http://www.w3.org/2001/XMLSchema}decimal"/>
 *         &lt;element name="KMEIN" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="KONWA" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ZSTR_PRICE_INFOType", propOrder = {
    "matnr",
    "maktx",
    "matkl",
    "lifnr",
    "name1",
    "ekorg",
    "esokz",
    "kbetr",
    "kpein",
    "kmein",
    "konwa"
})
public class ZSTRPRICEINFOType {

    @XmlElement(name = "MATNR", required = true)
    protected String matnr;
    @XmlElement(name = "MAKTX", required = true)
    protected String maktx;
    @XmlElement(name = "MATKL", required = true)
    protected String matkl;
    @XmlElement(name = "LIFNR", required = true)
    protected String lifnr;
    @XmlElement(name = "NAME1", required = true)
    protected String name1;
    @XmlElement(name = "EKORG", required = true)
    protected String ekorg;
    @XmlElement(name = "ESOKZ", required = true)
    protected String esokz;
    @XmlElement(name = "KBETR", required = true)
    protected BigDecimal kbetr;
    @XmlElement(name = "KPEIN", required = true)
    protected BigDecimal kpein;
    @XmlElement(name = "KMEIN", required = true)
    protected String kmein;
    @XmlElement(name = "KONWA", required = true)
    protected String konwa;

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
     * Gets the value of the maktx property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMAKTX() {
        return maktx;
    }

    /**
     * Sets the value of the maktx property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMAKTX(String value) {
        this.maktx = value;
    }

    /**
     * Gets the value of the matkl property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMATKL() {
        return matkl;
    }

    /**
     * Sets the value of the matkl property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMATKL(String value) {
        this.matkl = value;
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
     * Gets the value of the name1 property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNAME1() {
        return name1;
    }

    /**
     * Sets the value of the name1 property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNAME1(String value) {
        this.name1 = value;
    }

    /**
     * Gets the value of the ekorg property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getEKORG() {
        return ekorg;
    }

    /**
     * Sets the value of the ekorg property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setEKORG(String value) {
        this.ekorg = value;
    }

    /**
     * Gets the value of the esokz property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getESOKZ() {
        return esokz;
    }

    /**
     * Sets the value of the esokz property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setESOKZ(String value) {
        this.esokz = value;
    }

    /**
     * Gets the value of the kbetr property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getKBETR() {
        return kbetr;
    }

    /**
     * Sets the value of the kbetr property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setKBETR(BigDecimal value) {
        this.kbetr = value;
    }

    /**
     * Gets the value of the kpein property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getKPEIN() {
        return kpein;
    }

    /**
     * Sets the value of the kpein property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setKPEIN(BigDecimal value) {
        this.kpein = value;
    }

    /**
     * Gets the value of the kmein property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getKMEIN() {
        return kmein;
    }

    /**
     * Sets the value of the kmein property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setKMEIN(String value) {
        this.kmein = value;
    }

    /**
     * Gets the value of the konwa property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getKONWA() {
        return konwa;
    }

    /**
     * Sets the value of the konwa property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setKONWA(String value) {
        this.konwa = value;
    }

}
