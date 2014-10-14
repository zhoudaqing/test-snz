
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
 *         &lt;element name="OUTPUT" type="{http://www.example.org/QuerySupplierBalanceFromGVS/}ZFI_INT_LIFNR_YE_OUT" maxOccurs="unbounded" minOccurs="0"/>
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
    "output"
})
@XmlRootElement(name = "QuerySupplierBalanceFromGVSResponse")
public class QuerySupplierBalanceFromGVSResponse {

    @XmlElement(name = "OUTPUT")
    protected List<ZFIINTLIFNRYEOUT> output;

    /**
     * Gets the value of the output property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the output property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getOUTPUT().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link ZFIINTLIFNRYEOUT }
     * 
     * 
     */
    public List<ZFIINTLIFNRYEOUT> getOUTPUT() {
        if (output == null) {
            output = new ArrayList<ZFIINTLIFNRYEOUT>();
        }
        return this.output;
    }

}
