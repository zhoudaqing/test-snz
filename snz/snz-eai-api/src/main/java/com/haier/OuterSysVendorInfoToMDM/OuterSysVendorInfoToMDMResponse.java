
package com.haier.OuterSysVendorInfoToMDM;

import javax.xml.bind.annotation.*;


/**
 * <p>Java class for anonymous complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="RETCODE" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="RETMSG" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="OUT_ROW_ID" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="OUT_VENDOR_CODE" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="OUT_TAX_CODE" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "retcode",
    "retmsg",
    "outrowid",
    "outvendorcode",
    "outtaxcode"
})
@XmlRootElement(name = "OuterSysVendorInfoToMDMResponse")
public class OuterSysVendorInfoToMDMResponse {

    @XmlElement(name = "RETCODE", required = true)
    protected String retcode;
    @XmlElement(name = "RETMSG", required = true)
    protected String retmsg;
    @XmlElement(name = "OUT_ROW_ID", required = true)
    protected String outrowid;
    @XmlElement(name = "OUT_VENDOR_CODE", required = true)
    protected String outvendorcode;
    @XmlElement(name = "OUT_TAX_CODE", required = true)
    protected String outtaxcode;

    /**
     * Gets the value of the retcode property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRETCODE() {
        return retcode;
    }

    /**
     * Sets the value of the retcode property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRETCODE(String value) {
        this.retcode = value;
    }

    /**
     * Gets the value of the retmsg property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRETMSG() {
        return retmsg;
    }

    /**
     * Sets the value of the retmsg property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRETMSG(String value) {
        this.retmsg = value;
    }

    /**
     * Gets the value of the outrowid property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getOUTROWID() {
        return outrowid;
    }

    /**
     * Sets the value of the outrowid property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setOUTROWID(String value) {
        this.outrowid = value;
    }

    /**
     * Gets the value of the outvendorcode property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getOUTVENDORCODE() {
        return outvendorcode;
    }

    /**
     * Sets the value of the outvendorcode property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setOUTVENDORCODE(String value) {
        this.outvendorcode = value;
    }

    /**
     * Gets the value of the outtaxcode property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getOUTTAXCODE() {
        return outtaxcode;
    }

    /**
     * Sets the value of the outtaxcode property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setOUTTAXCODE(String value) {
        this.outtaxcode = value;
    }

}
