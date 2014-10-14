
package org.example.sendclaiminfotogvs;

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
 *         &lt;element name="T_INPUT" type="{http://www.example.org/SendClaimInfoToGVS/}ZSPJH_INPUT" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="T_INPUT2" type="{http://www.example.org/SendClaimInfoToGVS/}ZSPJH_INPUT_MONTHLY" maxOccurs="unbounded" minOccurs="0"/>
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
    "tinput",
    "tinput2"
})
@XmlRootElement(name = "SendClaimInfoToGVS")
public class SendClaimInfoToGVS_Type {

    @XmlElement(name = "T_INPUT")
    protected List<ZSPJHINPUT> tinput;
    @XmlElement(name = "T_INPUT2")
    protected List<ZSPJHINPUTMONTHLY> tinput2;

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
     * {@link ZSPJHINPUT }
     * 
     * 
     */
    public List<ZSPJHINPUT> getTINPUT() {
        if (tinput == null) {
            tinput = new ArrayList<ZSPJHINPUT>();
        }
        return this.tinput;
    }

    /**
     * Gets the value of the tinput2 property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the tinput2 property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getTINPUT2().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link ZSPJHINPUTMONTHLY }
     * 
     * 
     */
    public List<ZSPJHINPUTMONTHLY> getTINPUT2() {
        if (tinput2 == null) {
            tinput2 = new ArrayList<ZSPJHINPUTMONTHLY>();
        }
        return this.tinput2;
    }

}
