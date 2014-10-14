
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
 *         &lt;element name="MESSAGE" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="T_OUTPUT" type="{http://www.example.org/TransStandardCreditToSRM/}ZINT_ZDSP12LOG20_SENDING" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="EAIFlag" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="EAIMessage" type="{http://www.w3.org/2001/XMLSchema}string"/>
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
    "message",
    "toutput",
    "eaiFlag",
    "eaiMessage"
})
@XmlRootElement(name = "TransStandardCreditToSRMResponse")
public class TransStandardCreditToSRMResponse {

    @XmlElement(name = "MESSAGE", required = true)
    protected String message;
    @XmlElement(name = "T_OUTPUT")
    protected List<ZINTZDSP12LOG20SENDING> toutput;
    @XmlElement(name = "EAIFlag", required = true)
    protected String eaiFlag;
    @XmlElement(name = "EAIMessage", required = true)
    protected String eaiMessage;

    /**
     * Gets the value of the message property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMESSAGE() {
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
    public void setMESSAGE(String value) {
        this.message = value;
    }

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
     * {@link ZINTZDSP12LOG20SENDING }
     * 
     * 
     */
    public List<ZINTZDSP12LOG20SENDING> getTOUTPUT() {
        if (toutput == null) {
            toutput = new ArrayList<ZINTZDSP12LOG20SENDING>();
        }
        return this.toutput;
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

}
