
package com.haier.OuterSysVendorInfoToMDM;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import java.io.Serializable;


/**
 * <p>Java class for RSP_VENDOR_PUR_TYPE complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="RSP_VENDOR_PUR_TYPE">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="PURCHASE_GRP_CODE" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="ORDER_CURRENCY" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="PARTNER_PI" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "RSP_VENDOR_PUR_TYPE", propOrder = {
    "purchasegrpcode",
    "ordercurrency",
    "partnerpi"
})
public class RSPVENDORPURTYPE implements Serializable {

    private static final long serialVersionUID = 1074091650678254108L;
    @XmlElement(name = "PURCHASE_GRP_CODE", required = true)
    protected String purchasegrpcode;
    @XmlElement(name = "ORDER_CURRENCY", required = true)
    protected String ordercurrency;
    @XmlElement(name = "PARTNER_PI", required = true)
    protected String partnerpi;

    /**
     * Gets the value of the purchasegrpcode property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPURCHASEGRPCODE() {
        return purchasegrpcode;
    }

    /**
     * Sets the value of the purchasegrpcode property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPURCHASEGRPCODE(String value) {
        this.purchasegrpcode = value;
    }

    /**
     * Gets the value of the ordercurrency property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getORDERCURRENCY() {
        return ordercurrency;
    }

    /**
     * Sets the value of the ordercurrency property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setORDERCURRENCY(String value) {
        this.ordercurrency = value;
    }

    /**
     * Gets the value of the partnerpi property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPARTNERPI() {
        return partnerpi;
    }

    /**
     * Sets the value of the partnerpi property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPARTNERPI(String value) {
        this.partnerpi = value;
    }

}
