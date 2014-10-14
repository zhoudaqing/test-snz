
package com.haier.OuterSysVendorInfoToMDM;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


/**
 * <p>Java class for RSP_VENDOR_COMPANY_TABLE complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="RSP_VENDOR_COMPANY_TABLE">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="T_VENDOR_COMPANY_ITEM" type="{http://www.example.org/OuterSysVendorInfoToMDM/}RSP_VENDOR_COMPANY_TYPE" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "RSP_VENDOR_COMPANY_TABLE", propOrder = {
    "tvendorcompanyitem"
})
public class RSPVENDORCOMPANYTABLE implements Serializable {

    private static final long serialVersionUID = -5157042432241893171L;

    @XmlElement(name = "T_VENDOR_COMPANY_ITEM")
    protected List<RSPVENDORCOMPANYTYPE> tvendorcompanyitem;

    /**
     * Gets the value of the tvendorcompanyitem property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the tvendorcompanyitem property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getTVENDORCOMPANYITEM().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link RSPVENDORCOMPANYTYPE }
     * 
     * 
     */
    public List<RSPVENDORCOMPANYTYPE> getTVENDORCOMPANYITEM() {
        if (tvendorcompanyitem == null) {
            tvendorcompanyitem = new ArrayList<RSPVENDORCOMPANYTYPE>();
        }
        return this.tvendorcompanyitem;
    }

}
