
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
 *         &lt;element name="IN_VENDOR_CODE" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="IN_VENDOR_NAME" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="IN_ACCOUNT_GRP_CODE" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="IN_TAX_CODE" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="IN_STREET_ROOM" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="IN_POSTAL_CODE_CITY" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="IN_COUNTRY" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="IN_REGION" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="IN_PHONE_NUMBER" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="T_VENDOR_COMPANY" type="{http://www.example.org/OuterSysVendorInfoToMDM/}RSP_VENDOR_COMPANY_TABLE"/>
 *         &lt;element name="T_VENDOR_BANK" type="{http://www.example.org/OuterSysVendorInfoToMDM/}RSP_VENDOR_BANK_TABLE"/>
 *         &lt;element name="T_VENDOR_PUR" type="{http://www.example.org/OuterSysVendorInfoToMDM/}RSP_VENDOR_PUR_TABLE"/>
 *         &lt;element name="SYSTEM_NAME" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="VIEW_BANK_FLAG" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="OPERATE_TYPE" type="{http://www.w3.org/2001/XMLSchema}string"/>
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
    "invendorcode",
    "invendorname",
    "inaccountgrpcode",
    "intaxcode",
    "instreetroom",
    "inpostalcodecity",
    "incountry",
    "inregion",
    "inphonenumber",
    "tvendorcompany",
    "tvendorbank",
    "tvendorpur",
    "systemname",
    "viewbankflag",
    "operatetype"
})
@XmlRootElement(name = "OuterSysVendorInfoToMDM")
public class OuterSysVendorInfoToMDM_Type {

    @XmlElement(name = "IN_VENDOR_CODE", required = true)
    protected String invendorcode;
    @XmlElement(name = "IN_VENDOR_NAME", required = true)
    protected String invendorname;
    @XmlElement(name = "IN_ACCOUNT_GRP_CODE", required = true)
    protected String inaccountgrpcode;
    @XmlElement(name = "IN_TAX_CODE", required = true)
    protected String intaxcode;
    @XmlElement(name = "IN_STREET_ROOM", required = true)
    protected String instreetroom;
    @XmlElement(name = "IN_POSTAL_CODE_CITY", required = true)
    protected String inpostalcodecity;
    @XmlElement(name = "IN_COUNTRY", required = true)
    protected String incountry;
    @XmlElement(name = "IN_REGION", required = true)
    protected String inregion;
    @XmlElement(name = "IN_PHONE_NUMBER", required = true)
    protected String inphonenumber;
    @XmlElement(name = "T_VENDOR_COMPANY", required = true)
    protected RSPVENDORCOMPANYTABLE tvendorcompany;
    @XmlElement(name = "T_VENDOR_BANK", required = true)
    protected RSPVENDORBANKTABLE tvendorbank;
    @XmlElement(name = "T_VENDOR_PUR", required = true)
    protected RSPVENDORPURTABLE tvendorpur;
    @XmlElement(name = "SYSTEM_NAME", required = true)
    protected String systemname;
    @XmlElement(name = "VIEW_BANK_FLAG", required = true)
    protected String viewbankflag;
    @XmlElement(name = "OPERATE_TYPE", required = true)
    protected String operatetype;

    /**
     * Gets the value of the invendorcode property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getINVENDORCODE() {
        return invendorcode;
    }

    /**
     * Sets the value of the invendorcode property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setINVENDORCODE(String value) {
        this.invendorcode = value;
    }

    /**
     * Gets the value of the invendorname property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getINVENDORNAME() {
        return invendorname;
    }

    /**
     * Sets the value of the invendorname property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setINVENDORNAME(String value) {
        this.invendorname = value;
    }

    /**
     * Gets the value of the inaccountgrpcode property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getINACCOUNTGRPCODE() {
        return inaccountgrpcode;
    }

    /**
     * Sets the value of the inaccountgrpcode property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setINACCOUNTGRPCODE(String value) {
        this.inaccountgrpcode = value;
    }

    /**
     * Gets the value of the intaxcode property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getINTAXCODE() {
        return intaxcode;
    }

    /**
     * Sets the value of the intaxcode property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setINTAXCODE(String value) {
        this.intaxcode = value;
    }

    /**
     * Gets the value of the instreetroom property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getINSTREETROOM() {
        return instreetroom;
    }

    /**
     * Sets the value of the instreetroom property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setINSTREETROOM(String value) {
        this.instreetroom = value;
    }

    /**
     * Gets the value of the inpostalcodecity property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getINPOSTALCODECITY() {
        return inpostalcodecity;
    }

    /**
     * Sets the value of the inpostalcodecity property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setINPOSTALCODECITY(String value) {
        this.inpostalcodecity = value;
    }

    /**
     * Gets the value of the incountry property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getINCOUNTRY() {
        return incountry;
    }

    /**
     * Sets the value of the incountry property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setINCOUNTRY(String value) {
        this.incountry = value;
    }

    /**
     * Gets the value of the inregion property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getINREGION() {
        return inregion;
    }

    /**
     * Sets the value of the inregion property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setINREGION(String value) {
        this.inregion = value;
    }

    /**
     * Gets the value of the inphonenumber property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getINPHONENUMBER() {
        return inphonenumber;
    }

    /**
     * Sets the value of the inphonenumber property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setINPHONENUMBER(String value) {
        this.inphonenumber = value;
    }

    /**
     * Gets the value of the tvendorcompany property.
     * 
     * @return
     *     possible object is
     *     {@link RSPVENDORCOMPANYTABLE }
     *     
     */
    public RSPVENDORCOMPANYTABLE getTVENDORCOMPANY() {
        return tvendorcompany;
    }

    /**
     * Sets the value of the tvendorcompany property.
     * 
     * @param value
     *     allowed object is
     *     {@link RSPVENDORCOMPANYTABLE }
     *     
     */
    public void setTVENDORCOMPANY(RSPVENDORCOMPANYTABLE value) {
        this.tvendorcompany = value;
    }

    /**
     * Gets the value of the tvendorbank property.
     * 
     * @return
     *     possible object is
     *     {@link RSPVENDORBANKTABLE }
     *     
     */
    public RSPVENDORBANKTABLE getTVENDORBANK() {
        return tvendorbank;
    }

    /**
     * Sets the value of the tvendorbank property.
     * 
     * @param value
     *     allowed object is
     *     {@link RSPVENDORBANKTABLE }
     *     
     */
    public void setTVENDORBANK(RSPVENDORBANKTABLE value) {
        this.tvendorbank = value;
    }

    /**
     * Gets the value of the tvendorpur property.
     * 
     * @return
     *     possible object is
     *     {@link RSPVENDORPURTABLE }
     *     
     */
    public RSPVENDORPURTABLE getTVENDORPUR() {
        return tvendorpur;
    }

    /**
     * Sets the value of the tvendorpur property.
     * 
     * @param value
     *     allowed object is
     *     {@link RSPVENDORPURTABLE }
     *     
     */
    public void setTVENDORPUR(RSPVENDORPURTABLE value) {
        this.tvendorpur = value;
    }

    /**
     * Gets the value of the systemname property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSYSTEMNAME() {
        return systemname;
    }

    /**
     * Sets the value of the systemname property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSYSTEMNAME(String value) {
        this.systemname = value;
    }

    /**
     * Gets the value of the viewbankflag property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getVIEWBANKFLAG() {
        return viewbankflag;
    }

    /**
     * Sets the value of the viewbankflag property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setVIEWBANKFLAG(String value) {
        this.viewbankflag = value;
    }

    /**
     * Gets the value of the operatetype property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getOPERATETYPE() {
        return operatetype;
    }

    /**
     * Sets the value of the operatetype property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setOPERATETYPE(String value) {
        this.operatetype = value;
    }

}
