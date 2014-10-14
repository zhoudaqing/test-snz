
package org.example.transpricegoodsinfotohgvs;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import java.util.ArrayList;
import java.util.List;


/**
 * <p>Java class for OutType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="OutType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="TOTAL" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="TOTAL_BAK" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="UNDO" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="flag" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="message" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="faultDetail" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="DATAOUT" type="{http://www.example.org/TransPriceGoodsInfoToHGVS/}ZDWHJG_JHPT" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "OutType", propOrder = {
    "total",
    "totalbak",
    "undo",
    "flag",
    "message",
    "faultDetail",
    "dataout"
})
public class OutType {

    @XmlElement(name = "TOTAL")
    protected int total;
    @XmlElement(name = "TOTAL_BAK")
    protected int totalbak;
    @XmlElement(name = "UNDO")
    protected int undo;
    @XmlElement(required = true)
    protected String flag;
    @XmlElement(required = true)
    protected String message;
    @XmlElement(required = true)
    protected String faultDetail;
    @XmlElement(name = "DATAOUT")
    protected List<ZDWHJGJHPT> dataout;

    /**
     * Gets the value of the total property.
     * 
     */
    public int getTOTAL() {
        return total;
    }

    /**
     * Sets the value of the total property.
     * 
     */
    public void setTOTAL(int value) {
        this.total = value;
    }

    /**
     * Gets the value of the totalbak property.
     * 
     */
    public int getTOTALBAK() {
        return totalbak;
    }

    /**
     * Sets the value of the totalbak property.
     * 
     */
    public void setTOTALBAK(int value) {
        this.totalbak = value;
    }

    /**
     * Gets the value of the undo property.
     * 
     */
    public int getUNDO() {
        return undo;
    }

    /**
     * Sets the value of the undo property.
     * 
     */
    public void setUNDO(int value) {
        this.undo = value;
    }

    /**
     * Gets the value of the flag property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFlag() {
        return flag;
    }

    /**
     * Sets the value of the flag property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFlag(String value) {
        this.flag = value;
    }

    /**
     * Gets the value of the message property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMessage() {
        return message;
    }

    /**
     * Sets the value of the message property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMessage(String value) {
        this.message = value;
    }

    /**
     * Gets the value of the faultDetail property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFaultDetail() {
        return faultDetail;
    }

    /**
     * Sets the value of the faultDetail property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFaultDetail(String value) {
        this.faultDetail = value;
    }

    /**
     * Gets the value of the dataout property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the dataout property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getDATAOUT().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link org.example.transpricegoodsinfotohgvs.ZDWHJGJHPT }
     * 
     * 
     */
    public List<ZDWHJGJHPT> getDATAOUT() {
        if (dataout == null) {
            dataout = new ArrayList<ZDWHJGJHPT>();
        }
        return this.dataout;
    }

}
