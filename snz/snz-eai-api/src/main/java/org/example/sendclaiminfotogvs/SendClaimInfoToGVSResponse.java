
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
 *         &lt;element name="T_OUTPUT" type="{http://www.example.org/SendClaimInfoToGVS/}ZSPJH_OUTPUT" maxOccurs="unbounded" minOccurs="0"/>
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
    "toutput"
})
@XmlRootElement(name = "SendClaimInfoToGVSResponse")
public class SendClaimInfoToGVSResponse {

    @XmlElement(name = "T_OUTPUT")
    protected List<ZSPJHOUTPUT> toutput;

    /**
     * Gets the value of the toutput property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the toutput property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getTOUTPUT().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link ZSPJHOUTPUT }
     * 
     * 
     */
    public List<ZSPJHOUTPUT> getTOUTPUT() {
        if (toutput == null) {
            toutput = new ArrayList<ZSPJHOUTPUT>();
        }
        return this.toutput;
    }

}
