
package com.haier.OuterSysVendorInfoToMDM;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


/**
 * <p>Java class for RSP_VENDOR_BANK_TABLE complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="RSP_VENDOR_BANK_TABLE">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="T_VENDOR_BANK_ITEM" type="{http://www.example.org/OuterSysVendorInfoToMDM/}RSP_VENDOR_BANK_TYPE" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "RSP_VENDOR_BANK_TABLE", propOrder = {
    "tvendorbankitem"
})
public class RSPVENDORBANKTABLE implements Serializable {

    private static final long serialVersionUID = -165174226180130747L;

    @XmlElement(name = "T_VENDOR_BANK_ITEM")
    protected List<RSPVENDORBANKTYPE> tvendorbankitem;

    /**
     * Gets the value of the tvendorbankitem property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the tvendorbankitem property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getTVENDORBANKITEM().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link RSPVENDORBANKTYPE }
     * 
     * 
     */
    public List<RSPVENDORBANKTYPE> getTVENDORBANKITEM() {
        if (tvendorbankitem == null) {
            tvendorbankitem = new ArrayList<RSPVENDORBANKTYPE>();
        }
        return this.tvendorbankitem;
    }

}
