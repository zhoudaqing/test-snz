
package com.haier.OuterSysVendorInfoToMDM;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import java.io.Serializable;


/**
 * <p>Java class for RSP_VENDOR_COMPANY_TYPE complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="RSP_VENDOR_COMPANY_TYPE">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="COMPANY_CODE" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="RECONCILE_ACCOUNT" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="SEQUENCE_NO" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="PAYMENT_TERM" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="PAYMENT_METHOD" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="HQ" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="CASH_MGT_GROUP" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="ACCOUNTING_CUSTOMER" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="TOLERANCE_GROUP" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "RSP_VENDOR_COMPANY_TYPE", propOrder = {
    "companycode",
    "reconcileaccount",
    "sequenceno",
    "paymentterm",
    "paymentmethod",
    "hq",
    "cashmgtgroup",
    "accountingcustomer",
    "tolerancegroup"
})
public class RSPVENDORCOMPANYTYPE implements Serializable {

    private static final long serialVersionUID = 4607756794462083697L;

    @XmlElement(name = "COMPANY_CODE", required = true)
    protected String companycode;
    @XmlElement(name = "RECONCILE_ACCOUNT", required = true)
    protected String reconcileaccount;
    @XmlElement(name = "SEQUENCE_NO", required = true)
    protected String sequenceno;
    @XmlElement(name = "PAYMENT_TERM", required = true)
    protected String paymentterm;
    @XmlElement(name = "PAYMENT_METHOD", required = true)
    protected String paymentmethod;
    @XmlElement(name = "HQ", required = true)
    protected String hq;
    @XmlElement(name = "CASH_MGT_GROUP", required = true)
    protected String cashmgtgroup;
    @XmlElement(name = "ACCOUNTING_CUSTOMER", required = true)
    protected String accountingcustomer;
    @XmlElement(name = "TOLERANCE_GROUP", required = true)
    protected String tolerancegroup;

    /**
     * Gets the value of the companycode property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCOMPANYCODE() {
        return companycode;
    }

    /**
     * Sets the value of the companycode property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCOMPANYCODE(String value) {
        this.companycode = value;
    }

    /**
     * Gets the value of the reconcileaccount property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRECONCILEACCOUNT() {
        return reconcileaccount;
    }

    /**
     * Sets the value of the reconcileaccount property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRECONCILEACCOUNT(String value) {
        this.reconcileaccount = value;
    }

    /**
     * Gets the value of the sequenceno property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSEQUENCENO() {
        return sequenceno;
    }

    /**
     * Sets the value of the sequenceno property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSEQUENCENO(String value) {
        this.sequenceno = value;
    }

    /**
     * Gets the value of the paymentterm property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPAYMENTTERM() {
        return paymentterm;
    }

    /**
     * Sets the value of the paymentterm property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPAYMENTTERM(String value) {
        this.paymentterm = value;
    }

    /**
     * Gets the value of the paymentmethod property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPAYMENTMETHOD() {
        return paymentmethod;
    }

    /**
     * Sets the value of the paymentmethod property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPAYMENTMETHOD(String value) {
        this.paymentmethod = value;
    }

    /**
     * Gets the value of the hq property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getHQ() {
        return hq;
    }

    /**
     * Sets the value of the hq property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setHQ(String value) {
        this.hq = value;
    }

    /**
     * Gets the value of the cashmgtgroup property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCASHMGTGROUP() {
        return cashmgtgroup;
    }

    /**
     * Sets the value of the cashmgtgroup property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCASHMGTGROUP(String value) {
        this.cashmgtgroup = value;
    }

    /**
     * Gets the value of the accountingcustomer property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getACCOUNTINGCUSTOMER() {
        return accountingcustomer;
    }

    /**
     * Sets the value of the accountingcustomer property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setACCOUNTINGCUSTOMER(String value) {
        this.accountingcustomer = value;
    }

    /**
     * Gets the value of the tolerancegroup property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTOLERANCEGROUP() {
        return tolerancegroup;
    }

    /**
     * Sets the value of the tolerancegroup property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTOLERANCEGROUP(String value) {
        this.tolerancegroup = value;
    }

}
