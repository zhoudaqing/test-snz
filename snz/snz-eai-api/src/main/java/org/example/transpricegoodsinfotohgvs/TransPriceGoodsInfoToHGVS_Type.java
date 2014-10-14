
package org.example.transpricegoodsinfotohgvs;

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
 *         &lt;element name="DATAIN" type="{http://www.example.org/TransPriceGoodsInfoToHGVS/}ZDWH_JHPT" maxOccurs="unbounded" minOccurs="0"/>
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
    "datain"
})
@XmlRootElement(name = "TransPriceGoodsInfoToHGVS")
public class TransPriceGoodsInfoToHGVS_Type {

    @XmlElement(name = "DATAIN")
    protected List<ZDWHJHPT> datain;

    /**
     * Gets the value of the datain property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the datain property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getDATAIN().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link ZDWHJHPT }
     * 
     * 
     */
    public List<ZDWHJHPT> getDATAIN() {
        if (datain == null) {
            datain = new ArrayList<ZDWHJHPT>();
        }
        return this.datain;
    }

}
