
package org.example.transstandardcredittosrm;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import java.io.Serializable;
import java.math.BigDecimal;


/**
 * <p>Java class for ZINT_ZDSP12LOG20_SENDING complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="ZINT_ZDSP12LOG20_SENDING">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="AUFNR" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="WERKS" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="EBELN" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="EBELP" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="LIFNR" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="MATNR" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="XQDATE" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="SHOUHUO_TIME" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="YYDATE" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="NUM1" type="{http://www.w3.org/2001/XMLSchema}decimal"/>
 *         &lt;element name="ACTUAL_QTY" type="{http://www.w3.org/2001/XMLSchema}decimal"/>
 *         &lt;element name="NUMCHA" type="{http://www.w3.org/2001/XMLSchema}decimal"/>
 *         &lt;element name="REASONS" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="ZDATE" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="ZTIME" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ZINT_ZDSP12LOG20_SENDING", propOrder = {
    "aufnr",
    "werks",
    "ebeln",
    "ebelp",
    "lifnr",
    "matnr",
    "xqdate",
    "shouhuotime",
    "yydate",
    "num1",
    "actualqty",
    "numcha",
    "reasons",
    "zdate",
    "ztime"
})
public class ZINTZDSP12LOG20SENDING implements Serializable {

    private static final long serialVersionUID = -4438461406772258856L;

    @XmlElement(name = "AUFNR", required = true)
    protected String aufnr;
    @XmlElement(name = "WERKS", required = true)
    protected String werks;
    @XmlElement(name = "EBELN", required = true)
    protected String ebeln;
    @XmlElement(name = "EBELP", required = true)
    protected String ebelp;
    @XmlElement(name = "LIFNR", required = true)
    protected String lifnr;
    @XmlElement(name = "MATNR", required = true)
    protected String matnr;
    @XmlElement(name = "XQDATE", required = true)
    protected String xqdate;
    @XmlElement(name = "SHOUHUO_TIME", required = true)
    protected String shouhuotime;
    @XmlElement(name = "YYDATE", required = true)
    protected String yydate;
    @XmlElement(name = "NUM1", required = true)
    protected BigDecimal num1;
    @XmlElement(name = "ACTUAL_QTY", required = true)
    protected BigDecimal actualqty;
    @XmlElement(name = "NUMCHA", required = true)
    protected BigDecimal numcha;
    @XmlElement(name = "REASONS", required = true)
    protected String reasons;
    @XmlElement(name = "ZDATE", required = true)
    protected String zdate;
    @XmlElement(name = "ZTIME", required = true)
    protected String ztime;

    /**
     * Gets the value of the aufnr property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAUFNR() {
        return aufnr;
    }

    /**
     * Sets the value of the aufnr property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAUFNR(String value) {
        this.aufnr = value;
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
     * Gets the value of the ebeln property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getEBELN() {
        return ebeln;
    }

    /**
     * Sets the value of the ebeln property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setEBELN(String value) {
        this.ebeln = value;
    }

    /**
     * Gets the value of the ebelp property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getEBELP() {
        return ebelp;
    }

    /**
     * Sets the value of the ebelp property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setEBELP(String value) {
        this.ebelp = value;
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
     * Gets the value of the xqdate property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getXQDATE() {
        return xqdate;
    }

    /**
     * Sets the value of the xqdate property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setXQDATE(String value) {
        this.xqdate = value;
    }

    /**
     * Gets the value of the shouhuotime property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSHOUHUOTIME() {
        return shouhuotime;
    }

    /**
     * Sets the value of the shouhuotime property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSHOUHUOTIME(String value) {
        this.shouhuotime = value;
    }

    /**
     * Gets the value of the yydate property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getYYDATE() {
        return yydate;
    }

    /**
     * Sets the value of the yydate property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setYYDATE(String value) {
        this.yydate = value;
    }

    /**
     * Gets the value of the num1 property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getNUM1() {
        return num1;
    }

    /**
     * Sets the value of the num1 property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setNUM1(BigDecimal value) {
        this.num1 = value;
    }

    /**
     * Gets the value of the actualqty property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getACTUALQTY() {
        return actualqty;
    }

    /**
     * Sets the value of the actualqty property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setACTUALQTY(BigDecimal value) {
        this.actualqty = value;
    }

    /**
     * Gets the value of the numcha property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getNUMCHA() {
        return numcha;
    }

    /**
     * Sets the value of the numcha property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setNUMCHA(BigDecimal value) {
        this.numcha = value;
    }

    /**
     * Gets the value of the reasons property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getREASONS() {
        return reasons;
    }

    /**
     * Sets the value of the reasons property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setREASONS(String value) {
        this.reasons = value;
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

    /**
     * Gets the value of the ztime property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getZTIME() {
        return ztime;
    }

    /**
     * Sets the value of the ztime property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setZTIME(String value) {
        this.ztime = value;
    }

}
