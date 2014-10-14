
package com.haier.QuerySupplierBalanceFromGVS;

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
 *         &lt;element name="INPUT" type="{http://www.example.org/QuerySupplierBalanceFromGVS/}ZFI_INT_LIFNR_YE_IN" maxOccurs="unbounded" minOccurs="0"/>
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
    "input"
})
@XmlRootElement(name = "QuerySupplierBalanceFromGVS")
public class QuerySupplierBalanceFromGVS_Type {

    @XmlElement(name = "INPUT")
    protected List<ZFIINTLIFNRYEIN> input;

    /**
     * Gets the value of the input property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the input property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getINPUT().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link ZFIINTLIFNRYEIN }
     * 
     * 
     */
    public List<ZFIINTLIFNRYEIN> getINPUT() {
        if (input == null) {
            input = new ArrayList<ZFIINTLIFNRYEIN>();
        }
        return this.input;
    }

}
