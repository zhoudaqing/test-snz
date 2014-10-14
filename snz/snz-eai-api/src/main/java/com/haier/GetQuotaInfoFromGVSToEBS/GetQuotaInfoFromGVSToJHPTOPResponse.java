
package com.haier.GetQuotaInfoFromGVSToEBS;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


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
 *         &lt;element name="ZSTR_QUO_INFO" type="{http://www.example.org/GetQuotaInfoFromGVSToJHPT/}ZSTR_QUO_INFOType" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="ZSTR_PRICE_INFO" type="{http://www.example.org/GetQuotaInfoFromGVSToJHPT/}ZSTR_PRICE_INFOType" maxOccurs="unbounded" minOccurs="0"/>
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
    "zstrquoinfo",
    "zstrpriceinfo"
})
@XmlRootElement(name = "GetQuotaInfoFromGVSToJHPT_OPResponse")
public class GetQuotaInfoFromGVSToJHPTOPResponse {

    @XmlElement(name = "ZSTR_QUO_INFO")
    protected List<ZSTRQUOINFOType> zstrquoinfo;
    @XmlElement(name = "ZSTR_PRICE_INFO")
    protected List<ZSTRPRICEINFOType> zstrpriceinfo;

    /**
     * Gets the value of the zstrquoinfo property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the zstrquoinfo property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getZSTRQUOINFO().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link ZSTRQUOINFOType }
     * 
     * 
     */
    public List<ZSTRQUOINFOType> getZSTRQUOINFO() {
        if (zstrquoinfo == null) {
            zstrquoinfo = new ArrayList<ZSTRQUOINFOType>();
        }
        return this.zstrquoinfo;
    }

    /**
     * Gets the value of the zstrpriceinfo property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the zstrpriceinfo property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getZSTRPRICEINFO().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link ZSTRPRICEINFOType }
     * 
     * 
     */
    public List<ZSTRPRICEINFOType> getZSTRPRICEINFO() {
        if (zstrpriceinfo == null) {
            zstrpriceinfo = new ArrayList<ZSTRPRICEINFOType>();
        }
        return this.zstrpriceinfo;
    }

}
