
package com.haier.TransCashPledgeFromGVS;

import javax.xml.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;


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
 *         &lt;element name="ITAB_LOG" type="{http://www.example.org/TransCashPledgeFrom/}ZFI_MKZY_LOG" maxOccurs="unbounded" minOccurs="0"/>
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
    "itablog"
})
@XmlRootElement(name = "TransCashPledgeFrom")
public class TransCashPledgeFrom_Type {

    @XmlElement(name = "ITAB_LOG")
    protected List<ZFIMKZYLOG> itablog;

    /**
     * Gets the value of the itablog property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the itablog property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getITABLOG().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link ZFIMKZYLOG }
     * 
     * 
     */
    public List<ZFIMKZYLOG> getITABLOG() {
        if (itablog == null) {
            itablog = new ArrayList<ZFIMKZYLOG>();
        }
        return this.itablog;
    }

}
