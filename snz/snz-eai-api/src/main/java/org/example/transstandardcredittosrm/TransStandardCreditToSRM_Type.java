
package org.example.transstandardcredittosrm;

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
 *         &lt;element name="T_INPUT" type="{http://www.example.org/TransStandardCreditToSRM/}ZDSP12LOG20_INPUT" maxOccurs="unbounded" minOccurs="0"/>
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
    "tinput"
})
@XmlRootElement(name = "TransStandardCreditToSRM")
public class TransStandardCreditToSRM_Type {

    @XmlElement(name = "T_INPUT")
    protected List<ZDSP12LOG20INPUT> tinput;

    /**
     * Gets the value of the tinput property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the tinput property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getTINPUT().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link ZDSP12LOG20INPUT }
     * 
     * 
     */
    public List<ZDSP12LOG20INPUT> getTINPUT() {
        if (tinput == null) {
            tinput = new ArrayList<ZDSP12LOG20INPUT>();
        }
        return this.tinput;
    }

}
