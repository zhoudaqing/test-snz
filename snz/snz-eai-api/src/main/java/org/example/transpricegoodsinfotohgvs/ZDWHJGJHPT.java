
package org.example.transpricegoodsinfotohgvs;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import java.math.BigDecimal;


/**
 * <p>Java class for ZDWHJG_JHPT complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="ZDWHJG_JHPT">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="MANDT" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="IDNO" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="DO_DATE" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="DO_TIME" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="DO_TYPE" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="CODE" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="LOG_TYPE" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="MATNR" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="WERKS" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="TYPE" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="LIFNR" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="EKORG" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="QUOTE" type="{http://www.w3.org/2001/XMLSchema}decimal"/>
 *         &lt;element name="APLFZ" type="{http://www.w3.org/2001/XMLSchema}decimal"/>
 *         &lt;element name="EKGRP" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="NETPR" type="{http://www.w3.org/2001/XMLSchema}decimal"/>
 *         &lt;element name="ZOA1" type="{http://www.w3.org/2001/XMLSchema}decimal"/>
 *         &lt;element name="ZAF1" type="{http://www.w3.org/2001/XMLSchema}decimal"/>
 *         &lt;element name="INCO1" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="WAERS" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="KPEIN" type="{http://www.w3.org/2001/XMLSchema}decimal"/>
 *         &lt;element name="KMEIN" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="MEINS" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="WERKS_F" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="FLAGE" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="DATA_T" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="CHECK_R" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="ERROR" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="AGENT_FEE" type="{http://www.w3.org/2001/XMLSchema}decimal"/>
 *         &lt;element name="TAX" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ZDWHJG_JHPT", propOrder = {
    "mandt",
    "idno",
    "dodate",
    "dotime",
    "dotype",
    "code",
    "logtype",
    "matnr",
    "werks",
    "type",
    "lifnr",
    "ekorg",
    "quote",
    "aplfz",
    "ekgrp",
    "netpr",
    "zoa1",
    "zaf1",
    "inco1",
    "waers",
    "kpein",
    "kmein",
    "meins",
    "werksf",
    "flage",
    "datat",
    "checkr",
    "error",
    "agentfee",
    "tax"
})
public class ZDWHJGJHPT {

    @XmlElement(name = "MANDT", required = true)
    protected String mandt;
    @XmlElement(name = "IDNO", required = true)
    protected String idno;
    @XmlElement(name = "DO_DATE", required = true)
    protected String dodate;
    @XmlElement(name = "DO_TIME", required = true)
    protected String dotime;
    @XmlElement(name = "DO_TYPE", required = true)
    protected String dotype;
    @XmlElement(name = "CODE", required = true)
    protected String code;
    @XmlElement(name = "LOG_TYPE", required = true)
    protected String logtype;
    @XmlElement(name = "MATNR", required = true)
    protected String matnr;
    @XmlElement(name = "WERKS", required = true)
    protected String werks;
    @XmlElement(name = "TYPE", required = true)
    protected String type;
    @XmlElement(name = "LIFNR", required = true)
    protected String lifnr;
    @XmlElement(name = "EKORG", required = true)
    protected String ekorg;
    @XmlElement(name = "QUOTE", required = true)
    protected BigDecimal quote;
    @XmlElement(name = "APLFZ", required = true)
    protected BigDecimal aplfz;
    @XmlElement(name = "EKGRP", required = true)
    protected String ekgrp;
    @XmlElement(name = "NETPR", required = true)
    protected BigDecimal netpr;
    @XmlElement(name = "ZOA1", required = true)
    protected BigDecimal zoa1;
    @XmlElement(name = "ZAF1", required = true)
    protected BigDecimal zaf1;
    @XmlElement(name = "INCO1", required = true)
    protected String inco1;
    @XmlElement(name = "WAERS", required = true)
    protected String waers;
    @XmlElement(name = "KPEIN", required = true)
    protected BigDecimal kpein;
    @XmlElement(name = "KMEIN", required = true)
    protected String kmein;
    @XmlElement(name = "MEINS", required = true)
    protected String meins;
    @XmlElement(name = "WERKS_F", required = true)
    protected String werksf;
    @XmlElement(name = "FLAGE", required = true)
    protected String flage;
    @XmlElement(name = "DATA_T", required = true)
    protected String datat;
    @XmlElement(name = "CHECK_R", required = true)
    protected String checkr;
    @XmlElement(name = "ERROR", required = true)
    protected String error;
    @XmlElement(name = "AGENT_FEE", required = true)
    protected BigDecimal agentfee;
    @XmlElement(name = "TAX", required = true)
    protected String tax;

    /**
     * Gets the value of the mandt property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMANDT() {
        return mandt;
    }

    /**
     * Sets the value of the mandt property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMANDT(String value) {
        this.mandt = value;
    }

    /**
     * Gets the value of the idno property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIDNO() {
        return idno;
    }

    /**
     * Sets the value of the idno property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIDNO(String value) {
        this.idno = value;
    }

    /**
     * Gets the value of the dodate property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDODATE() {
        return dodate;
    }

    /**
     * Sets the value of the dodate property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDODATE(String value) {
        this.dodate = value;
    }

    /**
     * Gets the value of the dotime property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDOTIME() {
        return dotime;
    }

    /**
     * Sets the value of the dotime property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDOTIME(String value) {
        this.dotime = value;
    }

    /**
     * Gets the value of the dotype property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDOTYPE() {
        return dotype;
    }

    /**
     * Sets the value of the dotype property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDOTYPE(String value) {
        this.dotype = value;
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
     * Gets the value of the logtype property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getLOGTYPE() {
        return logtype;
    }

    /**
     * Sets the value of the logtype property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setLOGTYPE(String value) {
        this.logtype = value;
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
     * Gets the value of the type property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTYPE() {
        return type;
    }

    /**
     * Sets the value of the type property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTYPE(String value) {
        this.type = value;
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
     * Gets the value of the quote property.
     * 
     * @return
     *     possible object is
     *     {@link java.math.BigDecimal }
     *     
     */
    public BigDecimal getQUOTE() {
        return quote;
    }

    /**
     * Sets the value of the quote property.
     * 
     * @param value
     *     allowed object is
     *     {@link java.math.BigDecimal }
     *     
     */
    public void setQUOTE(BigDecimal value) {
        this.quote = value;
    }

    /**
     * Gets the value of the aplfz property.
     * 
     * @return
     *     possible object is
     *     {@link java.math.BigDecimal }
     *     
     */
    public BigDecimal getAPLFZ() {
        return aplfz;
    }

    /**
     * Sets the value of the aplfz property.
     * 
     * @param value
     *     allowed object is
     *     {@link java.math.BigDecimal }
     *     
     */
    public void setAPLFZ(BigDecimal value) {
        this.aplfz = value;
    }

    /**
     * Gets the value of the ekgrp property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getEKGRP() {
        return ekgrp;
    }

    /**
     * Sets the value of the ekgrp property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setEKGRP(String value) {
        this.ekgrp = value;
    }

    /**
     * Gets the value of the netpr property.
     * 
     * @return
     *     possible object is
     *     {@link java.math.BigDecimal }
     *     
     */
    public BigDecimal getNETPR() {
        return netpr;
    }

    /**
     * Sets the value of the netpr property.
     * 
     * @param value
     *     allowed object is
     *     {@link java.math.BigDecimal }
     *     
     */
    public void setNETPR(BigDecimal value) {
        this.netpr = value;
    }

    /**
     * Gets the value of the zoa1 property.
     * 
     * @return
     *     possible object is
     *     {@link java.math.BigDecimal }
     *     
     */
    public BigDecimal getZOA1() {
        return zoa1;
    }

    /**
     * Sets the value of the zoa1 property.
     * 
     * @param value
     *     allowed object is
     *     {@link java.math.BigDecimal }
     *     
     */
    public void setZOA1(BigDecimal value) {
        this.zoa1 = value;
    }

    /**
     * Gets the value of the zaf1 property.
     * 
     * @return
     *     possible object is
     *     {@link java.math.BigDecimal }
     *     
     */
    public BigDecimal getZAF1() {
        return zaf1;
    }

    /**
     * Sets the value of the zaf1 property.
     * 
     * @param value
     *     allowed object is
     *     {@link java.math.BigDecimal }
     *     
     */
    public void setZAF1(BigDecimal value) {
        this.zaf1 = value;
    }

    /**
     * Gets the value of the inco1 property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getINCO1() {
        return inco1;
    }

    /**
     * Sets the value of the inco1 property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setINCO1(String value) {
        this.inco1 = value;
    }

    /**
     * Gets the value of the waers property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getWAERS() {
        return waers;
    }

    /**
     * Sets the value of the waers property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setWAERS(String value) {
        this.waers = value;
    }

    /**
     * Gets the value of the kpein property.
     * 
     * @return
     *     possible object is
     *     {@link java.math.BigDecimal }
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
     *     {@link java.math.BigDecimal }
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
     * Gets the value of the meins property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMEINS() {
        return meins;
    }

    /**
     * Sets the value of the meins property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMEINS(String value) {
        this.meins = value;
    }

    /**
     * Gets the value of the werksf property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getWERKSF() {
        return werksf;
    }

    /**
     * Sets the value of the werksf property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setWERKSF(String value) {
        this.werksf = value;
    }

    /**
     * Gets the value of the flage property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFLAGE() {
        return flage;
    }

    /**
     * Sets the value of the flage property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFLAGE(String value) {
        this.flage = value;
    }

    /**
     * Gets the value of the datat property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDATAT() {
        return datat;
    }

    /**
     * Sets the value of the datat property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDATAT(String value) {
        this.datat = value;
    }

    /**
     * Gets the value of the checkr property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCHECKR() {
        return checkr;
    }

    /**
     * Sets the value of the checkr property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCHECKR(String value) {
        this.checkr = value;
    }

    /**
     * Gets the value of the error property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getERROR() {
        return error;
    }

    /**
     * Sets the value of the error property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setERROR(String value) {
        this.error = value;
    }

    /**
     * Gets the value of the agentfee property.
     * 
     * @return
     *     possible object is
     *     {@link java.math.BigDecimal }
     *     
     */
    public BigDecimal getAGENTFEE() {
        return agentfee;
    }

    /**
     * Sets the value of the agentfee property.
     * 
     * @param value
     *     allowed object is
     *     {@link java.math.BigDecimal }
     *     
     */
    public void setAGENTFEE(BigDecimal value) {
        this.agentfee = value;
    }

    /**
     * Gets the value of the tax property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTAX() {
        return tax;
    }

    /**
     * Sets the value of the tax property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTAX(String value) {
        this.tax = value;
    }

}
