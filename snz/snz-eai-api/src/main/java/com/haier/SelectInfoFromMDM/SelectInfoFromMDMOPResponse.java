
package com.haier.SelectInfoFromMDM;

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
 *         &lt;element name="Output">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element name="out" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *                   &lt;element name="PageCount" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *                   &lt;element name="CurrentPage" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *                 &lt;/sequence>
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
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
    "output"
})
@XmlRootElement(name = "SelectInfoFromMDM_OPResponse")
public class SelectInfoFromMDMOPResponse {

    @XmlElement(name = "Output", required = true)
    protected SelectInfoFromMDMOPResponse.Output output;

    /**
     * Gets the value of the output property.
     * 
     * @return
     *     possible object is
     *     {@link SelectInfoFromMDMOPResponse.Output }
     *     
     */
    public SelectInfoFromMDMOPResponse.Output getOutput() {
        return output;
    }

    /**
     * Sets the value of the output property.
     * 
     * @param value
     *     allowed object is
     *     {@link SelectInfoFromMDMOPResponse.Output }
     *     
     */
    public void setOutput(SelectInfoFromMDMOPResponse.Output value) {
        this.output = value;
    }


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
     *         &lt;element name="out" type="{http://www.w3.org/2001/XMLSchema}string"/>
     *         &lt;element name="PageCount" type="{http://www.w3.org/2001/XMLSchema}string"/>
     *         &lt;element name="CurrentPage" type="{http://www.w3.org/2001/XMLSchema}string"/>
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
        "out",
        "pageCount",
        "currentPage"
    })
    public static class Output {

        @XmlElement(required = true)
        protected String out;
        @XmlElement(name = "PageCount", required = true)
        protected String pageCount;
        @XmlElement(name = "CurrentPage", required = true)
        protected String currentPage;

        /**
         * Gets the value of the out property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getOut() {
            return out;
        }

        /**
         * Sets the value of the out property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setOut(String value) {
            this.out = value;
        }

        /**
         * Gets the value of the pageCount property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getPageCount() {
            return pageCount;
        }

        /**
         * Sets the value of the pageCount property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setPageCount(String value) {
            this.pageCount = value;
        }

        /**
         * Gets the value of the currentPage property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getCurrentPage() {
            return currentPage;
        }

        /**
         * Sets the value of the currentPage property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setCurrentPage(String value) {
            this.currentPage = value;
        }

    }

}
