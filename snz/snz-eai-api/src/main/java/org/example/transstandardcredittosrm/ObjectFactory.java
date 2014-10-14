
package org.example.transstandardcredittosrm;

import javax.xml.bind.annotation.XmlRegistry;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the org.example.transstandardcredittosrm package. 
 * <p>An ObjectFactory allows you to programatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {


    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: org.example.transstandardcredittosrm
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link TransStandardCreditToSRM_Type }
     * 
     */
    public TransStandardCreditToSRM_Type createTransStandardCreditToSRM_Type() {
        return new TransStandardCreditToSRM_Type();
    }

    /**
     * Create an instance of {@link TransStandardCreditToSRMResponse }
     * 
     */
    public TransStandardCreditToSRMResponse createTransStandardCreditToSRMResponse() {
        return new TransStandardCreditToSRMResponse();
    }

    /**
     * Create an instance of {@link ZDSP12LOG20INPUT }
     * 
     */
    public ZDSP12LOG20INPUT createZDSP12LOG20INPUT() {
        return new ZDSP12LOG20INPUT();
    }

    /**
     * Create an instance of {@link ZINTZDSP12LOG20SENDING }
     * 
     */
    public ZINTZDSP12LOG20SENDING createZINTZDSP12LOG20SENDING() {
        return new ZINTZDSP12LOG20SENDING();
    }

}
