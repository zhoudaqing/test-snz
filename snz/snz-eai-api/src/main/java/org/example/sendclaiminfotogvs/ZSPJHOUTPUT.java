
package org.example.sendclaiminfotogvs;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import java.io.Serializable;
import java.math.BigDecimal;


/**
 * <p>Java class for ZSPJH_OUTPUT complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="ZSPJH_OUTPUT">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="ZYY" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="ZCYX" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="LIFNR" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="NAME1" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="EX_IN_FLAG" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="WERKS" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="MATNR" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="MAKTX" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="EKGRP" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="EKNAM" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="KOUFEN" type="{http://www.w3.org/2001/XMLSchema}decimal"/>
 *         &lt;element name="ZKKJE" type="{http://www.w3.org/2001/XMLSchema}decimal"/>
 *         &lt;element name="ZKKDATA" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="ZDATE" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="ZTIME" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="ZKKFLG" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="ZFFLG" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="ZFMAS" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="ZFDAT" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="EAIFlag" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="EAIMessage" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="EAIDetail" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ZSPJH_OUTPUT", propOrder = {
    "zyy",
    "zcyx",
    "lifnr",
    "name1",
    "exinflag",
    "werks",
    "matnr",
    "maktx",
    "ekgrp",
    "eknam",
    "koufen",
    "zkkje",
    "zkkdata",
    "zdate",
    "ztime",
    "zkkflg",
    "zfflg",
    "zfmas",
    "zfdat",
    "eaiFlag",
    "eaiMessage",
    "eaiDetail"
})
public class ZSPJHOUTPUT implements Serializable {

    private static final long serialVersionUID = 5722987578864934491L;
    @XmlElement(name = "ZYY", required = true)
    protected String zyy;
    @XmlElement(name = "ZCYX", required = true)
    protected String zcyx;
    @XmlElement(name = "LIFNR", required = true)
    protected String lifnr;
    @XmlElement(name = "NAME1", required = true)
    protected String name1;
    @XmlElement(name = "EX_IN_FLAG", required = true)
    protected String exinflag;
    @XmlElement(name = "WERKS", required = true)
    protected String werks;
    @XmlElement(name = "MATNR", required = true)
    protected String matnr;
    @XmlElement(name = "MAKTX", required = true)
    protected String maktx;
    @XmlElement(name = "EKGRP", required = true)
    protected String ekgrp;
    @XmlElement(name = "EKNAM", required = true)
    protected String eknam;
    @XmlElement(name = "KOUFEN", required = true)
    protected BigDecimal koufen;
    @XmlElement(name = "ZKKJE", required = true)
    protected BigDecimal zkkje;
    @XmlElement(name = "ZKKDATA", required = true)
    protected String zkkdata;
    @XmlElement(name = "ZDATE", required = true)
    protected String zdate;
    @XmlElement(name = "ZTIME", required = true)
    protected String ztime;
    @XmlElement(name = "ZKKFLG", required = true)
    protected String zkkflg;
    @XmlElement(name = "ZFFLG", required = true)
    protected String zfflg;
    @XmlElement(name = "ZFMAS", required = true)
    protected String zfmas;
    @XmlElement(name = "ZFDAT", required = true)
    protected String zfdat;
    @XmlElement(name = "EAIFlag", required = true)
    protected String eaiFlag;
    @XmlElement(name = "EAIMessage", required = true)
    protected String eaiMessage;
    @XmlElement(name = "EAIDetail", required = true)
    protected String eaiDetail;

    /**
     * Gets the value of the zyy property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getZYY() {
        return zyy;
    }

    /**
     * Sets the value of the zyy property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setZYY(String value) {
        this.zyy = value;
    }

    /**
     * Gets the value of the zcyx property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getZCYX() {
        return zcyx;
    }

    /**
     * Sets the value of the zcyx property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setZCYX(String value) {
        this.zcyx = value;
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
     * Gets the value of the exinflag property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getEXINFLAG() {
        return exinflag;
    }

    /**
     * Sets the value of the exinflag property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setEXINFLAG(String value) {
        this.exinflag = value;
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
     * Gets the value of the eknam property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getEKNAM() {
        return eknam;
    }

    /**
     * Sets the value of the eknam property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setEKNAM(String value) {
        this.eknam = value;
    }

    /**
     * Gets the value of the koufen property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getKOUFEN() {
        return koufen;
    }

    /**
     * Sets the value of the koufen property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setKOUFEN(BigDecimal value) {
        this.koufen = value;
    }

    /**
     * Gets the value of the zkkje property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getZKKJE() {
        return zkkje;
    }

    /**
     * Sets the value of the zkkje property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setZKKJE(BigDecimal value) {
        this.zkkje = value;
    }

    /**
     * Gets the value of the zkkdata property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getZKKDATA() {
        return zkkdata;
    }

    /**
     * Sets the value of the zkkdata property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setZKKDATA(String value) {
        this.zkkdata = value;
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

    /**
     * Gets the value of the zkkflg property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getZKKFLG() {
        return zkkflg;
    }

    /**
     * Sets the value of the zkkflg property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setZKKFLG(String value) {
        this.zkkflg = value;
    }

    /**
     * Gets the value of the zfflg property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getZFFLG() {
        return zfflg;
    }

    /**
     * Sets the value of the zfflg property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setZFFLG(String value) {
        this.zfflg = value;
    }

    /**
     * Gets the value of the zfmas property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getZFMAS() {
        return zfmas;
    }

    /**
     * Sets the value of the zfmas property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setZFMAS(String value) {
        this.zfmas = value;
    }

    /**
     * Gets the value of the zfdat property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getZFDAT() {
        return zfdat;
    }

    /**
     * Sets the value of the zfdat property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setZFDAT(String value) {
        this.zfdat = value;
    }

    /**
     * Gets the value of the eaiFlag property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getEAIFlag() {
        return eaiFlag;
    }

    /**
     * Sets the value of the eaiFlag property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setEAIFlag(String value) {
        this.eaiFlag = value;
    }

    /**
     * Gets the value of the eaiMessage property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getEAIMessage() {
        return eaiMessage;
    }

    /**
     * Sets the value of the eaiMessage property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setEAIMessage(String value) {
        this.eaiMessage = value;
    }

    /**
     * Gets the value of the eaiDetail property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getEAIDetail() {
        return eaiDetail;
    }

    /**
     * Sets the value of the eaiDetail property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setEAIDetail(String value) {
        this.eaiDetail = value;
    }

}
