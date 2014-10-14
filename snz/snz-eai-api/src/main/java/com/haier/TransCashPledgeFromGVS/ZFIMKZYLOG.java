
package com.haier.TransCashPledgeFromGVS;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import java.math.BigDecimal;


/**
 * <p>Java class for ZFI_MKZY_LOG complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="ZFI_MKZY_LOG">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="XBLNR" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="CODE" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="GJAHR" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="BUKRS" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="MONAT" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="BUDAT" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="LIFNR" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="DEDAT" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="DMBTR" type="{http://www.w3.org/2001/XMLSchema}decimal"/>
 *         &lt;element name="LX" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="CPUDT" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="CPUTM" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="BELNR" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="FLAG" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="MESSAGE" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ZFI_MKZY_LOG", propOrder = {
    "xblnr",
    "code",
    "gjahr",
    "bukrs",
    "monat",
    "budat",
    "lifnr",
    "dedat",
    "dmbtr",
    "lx",
    "cpudt",
    "cputm",
    "belnr",
    "flag",
    "message"
})
public class ZFIMKZYLOG {

    @XmlElement(name = "XBLNR", required = true)
    protected String xblnr;
    @XmlElement(name = "CODE", required = true)
    protected String code;
    @XmlElement(name = "GJAHR", required = true)
    protected String gjahr;
    @XmlElement(name = "BUKRS", required = true)
    protected String bukrs;
    @XmlElement(name = "MONAT", required = true)
    protected String monat;
    @XmlElement(name = "BUDAT", required = true)
    protected String budat;
    @XmlElement(name = "LIFNR", required = true)
    protected String lifnr;
    @XmlElement(name = "DEDAT", required = true)
    protected String dedat;
    @XmlElement(name = "DMBTR", required = true)
    protected BigDecimal dmbtr;
    @XmlElement(name = "LX", required = true)
    protected String lx;
    @XmlElement(name = "CPUDT", required = true)
    protected String cpudt;
    @XmlElement(name = "CPUTM", required = true)
    protected String cputm;
    @XmlElement(name = "BELNR", required = true)
    protected String belnr;
    @XmlElement(name = "FLAG", required = true)
    protected String flag;
    @XmlElement(name = "MESSAGE", required = true)
    protected String message;

    /**
     * Gets the value of the xblnr property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getXBLNR() {
        return xblnr;
    }

    /**
     * Sets the value of the xblnr property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setXBLNR(String value) {
        this.xblnr = value;
    }

    /**
     * Gets the value of the code property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCODE() {
        return code;
    }

    /**
     * Sets the value of the code property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCODE(String value) {
        this.code = value;
    }

    /**
     * Gets the value of the gjahr property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getGJAHR() {
        return gjahr;
    }

    /**
     * Sets the value of the gjahr property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setGJAHR(String value) {
        this.gjahr = value;
    }

    /**
     * Gets the value of the bukrs property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getBUKRS() {
        return bukrs;
    }

    /**
     * Sets the value of the bukrs property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setBUKRS(String value) {
        this.bukrs = value;
    }

    /**
     * Gets the value of the monat property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMONAT() {
        return monat;
    }

    /**
     * Sets the value of the monat property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMONAT(String value) {
        this.monat = value;
    }

    /**
     * Gets the value of the budat property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getBUDAT() {
        return budat;
    }

    /**
     * Sets the value of the budat property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setBUDAT(String value) {
        this.budat = value;
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
     * Gets the value of the dedat property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDEDAT() {
        return dedat;
    }

    /**
     * Sets the value of the dedat property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDEDAT(String value) {
        this.dedat = value;
    }

    /**
     * Gets the value of the dmbtr property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getDMBTR() {
        return dmbtr;
    }

    /**
     * Sets the value of the dmbtr property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setDMBTR(BigDecimal value) {
        this.dmbtr = value;
    }

    /**
     * Gets the value of the lx property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getLX() {
        return lx;
    }

    /**
     * Sets the value of the lx property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setLX(String value) {
        this.lx = value;
    }

    /**
     * Gets the value of the cpudt property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCPUDT() {
        return cpudt;
    }

    /**
     * Sets the value of the cpudt property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCPUDT(String value) {
        this.cpudt = value;
    }

    /**
     * Gets the value of the cputm property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCPUTM() {
        return cputm;
    }

    /**
     * Sets the value of the cputm property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCPUTM(String value) {
        this.cputm = value;
    }

    /**
     * Gets the value of the belnr property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getBELNR() {
        return belnr;
    }

    /**
     * Sets the value of the belnr property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setBELNR(String value) {
        this.belnr = value;
    }

    /**
     * Gets the value of the flag property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFLAG() {
        return flag;
    }

    /**
     * Sets the value of the flag property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFLAG(String value) {
        this.flag = value;
    }

    /**
     * Gets the value of the message property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMESSAGE() {
        return message;
    }

    /**
     * Sets the value of the message property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMESSAGE(String value) {
        this.message = value;
    }

}
