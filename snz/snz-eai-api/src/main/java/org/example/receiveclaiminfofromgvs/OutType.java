
package org.example.receiveclaiminfofromgvs;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


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
 *         &lt;element name="Z_RET" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="EAIFlag" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="EAIMessage" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="EAIDetail" type="{http://www.w3.org/2001/XMLSchema}string"/>
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
    "zret",
    "eaiFlag",
    "eaiMessage",
    "eaiDetail"
})
public class OutType {

    @XmlElement(name = "Z_RET", required = true)
    protected String zret;
    @XmlElement(name = "EAIFlag", required = true)
    protected String eaiFlag;
    @XmlElement(name = "EAIMessage", required = true)
    protected String eaiMessage;
    @XmlElement(name = "EAIDetail", required = true)
    protected String eaiDetail;

    /**
     * Gets the value of the zret property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getZRET() {
        return zret;
    }

    /**
     * Sets the value of the zret property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setZRET(String value) {
        this.zret = value;
    }

    /**
     * Gets the value of the eaiFlag property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getEAIFlag() {
        return eaiFlag;
    }

    /**
     * Sets the value of the eaiFlag property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setEAIFlag(String value) {
        this.eaiFlag = value;
    }

    /**
     * Gets the value of the eaiMessage property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getEAIMessage() {
        return eaiMessage;
    }

    /**
     * Sets the value of the eaiMessage property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setEAIMessage(String value) {
        this.eaiMessage = value;
    }

    /**
     * Gets the value of the eaiDetail property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getEAIDetail() {
        return eaiDetail;
    }

    /**
     * Sets the value of the eaiDetail property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setEAIDetail(String value) {
        this.eaiDetail = value;
    }

}
