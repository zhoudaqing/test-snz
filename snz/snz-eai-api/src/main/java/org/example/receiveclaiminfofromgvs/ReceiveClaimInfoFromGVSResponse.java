
package org.example.receiveclaiminfofromgvs;

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
 *         &lt;element name="out" type="{http://www.example.org/ReceiveClaimInfoFromGVS/}OutType"/>
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
    "out"
})
@XmlRootElement(name = "ReceiveClaimInfoFromGVSResponse")
public class ReceiveClaimInfoFromGVSResponse {

    @XmlElement(required = true)
    protected OutType out;

    /**
     * Gets the value of the out property.
     * 
     * @return
     *     possible object is
     *     {@link OutType }
     *     
     */
    public OutType getOut() {
        return out;
    }

    /**
     * Sets the value of the out property.
     * 
     * @param value
     *     allowed object is
     *     {@link OutType }
     *     
     */
    public void setOut(OutType value) {
        this.out = value;
    }

}
