
package org.example.transpricegoodsinfotohgvs;

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
 *         &lt;element name="out" type="{http://www.example.org/TransPriceGoodsInfoToHGVS/}OutType"/>
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
@XmlRootElement(name = "TransPriceGoodsInfoToHGVSResponse")
public class TransPriceGoodsInfoToHGVSResponse {

    @XmlElement(required = true)
    protected OutType out;

    /**
     * Gets the value of the out property.
     * 
     * @return
     *     possible object is
     *     {@link org.example.transpricegoodsinfotohgvs.OutType }
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
     *     {@link org.example.transpricegoodsinfotohgvs.OutType }
     *     
     */
    public void setOut(OutType value) {
        this.out = value;
    }

}
