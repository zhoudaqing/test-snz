
package com.haier.OuterSysVendorInfoToMDM;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import java.io.Serializable;


/**
 * <p>Java class for RSP_VENDOR_BANK_TYPE complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="RSP_VENDOR_BANK_TYPE">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="ACCOUNT_HOLDER_NAME" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="BANK_COUNTRY" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="BANK_ACCOUNT_NUM" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="BANK_BRANCH_CODE" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "RSP_VENDOR_BANK_TYPE", propOrder = {
    "accountholdername",
    "bankcountry",
    "bankaccountnum",
    "bankbranchcode"
})
public class RSPVENDORBANKTYPE implements Serializable {

    private static final long serialVersionUID = -7877525569972222210L;

    @XmlElement(name = "ACCOUNT_HOLDER_NAME", required = true)
    protected String accountholdername;
    @XmlElement(name = "BANK_COUNTRY", required = true)
    protected String bankcountry;
    @XmlElement(name = "BANK_ACCOUNT_NUM", required = true)
    protected String bankaccountnum;
    @XmlElement(name = "BANK_BRANCH_CODE", required = true)
    protected String bankbranchcode;

    /**
     * Gets the value of the accountholdername property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getACCOUNTHOLDERNAME() {
        return accountholdername;
    }

    /**
     * Sets the value of the accountholdername property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setACCOUNTHOLDERNAME(String value) {
        this.accountholdername = value;
    }

    /**
     * Gets the value of the bankcountry property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getBANKCOUNTRY() {
        return bankcountry;
    }

    /**
     * Sets the value of the bankcountry property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setBANKCOUNTRY(String value) {
        this.bankcountry = value;
    }

    /**
     * Gets the value of the bankaccountnum property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getBANKACCOUNTNUM() {
        return bankaccountnum;
    }

    /**
     * Sets the value of the bankaccountnum property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setBANKACCOUNTNUM(String value) {
        this.bankaccountnum = value;
    }

    /**
     * Gets the value of the bankbranchcode property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getBANKBRANCHCODE() {
        return bankbranchcode;
    }

    /**
     * Sets the value of the bankbranchcode property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setBANKBRANCHCODE(String value) {
        this.bankbranchcode = value;
    }

}
