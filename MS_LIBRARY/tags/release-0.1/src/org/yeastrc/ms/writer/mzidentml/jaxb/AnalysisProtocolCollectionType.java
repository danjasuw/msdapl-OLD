//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vJAXB 2.1.10 in JDK 6 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2011.08.01 at 02:54:01 PM PDT 
//


package org.yeastrc.ms.writer.mzidentml.jaxb;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


/**
 * The collection of protocols which include the parameters and settings of the performed analyses. 
 * 
 * <p>Java class for AnalysisProtocolCollectionType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="AnalysisProtocolCollectionType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="SpectrumIdentificationProtocol" type="{http://psidev.info/psi/pi/mzIdentML/1.1}SpectrumIdentificationProtocolType" maxOccurs="unbounded"/>
 *         &lt;element name="ProteinDetectionProtocol" type="{http://psidev.info/psi/pi/mzIdentML/1.1}ProteinDetectionProtocolType" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "AnalysisProtocolCollectionType", propOrder = {
    "spectrumIdentificationProtocol",
    "proteinDetectionProtocol"
})
@XmlRootElement(name="AnalysisProtocolCollection")
public class AnalysisProtocolCollectionType {

    @XmlElement(name = "SpectrumIdentificationProtocol", required = true)
    protected List<SpectrumIdentificationProtocolType> spectrumIdentificationProtocol;
    @XmlElement(name = "ProteinDetectionProtocol")
    protected ProteinDetectionProtocolType proteinDetectionProtocol;

    /**
     * Gets the value of the spectrumIdentificationProtocol property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the spectrumIdentificationProtocol property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getSpectrumIdentificationProtocol().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link SpectrumIdentificationProtocolType }
     * 
     * 
     */
    public List<SpectrumIdentificationProtocolType> getSpectrumIdentificationProtocol() {
        if (spectrumIdentificationProtocol == null) {
            spectrumIdentificationProtocol = new ArrayList<SpectrumIdentificationProtocolType>();
        }
        return this.spectrumIdentificationProtocol;
    }

    /**
     * Gets the value of the proteinDetectionProtocol property.
     * 
     * @return
     *     possible object is
     *     {@link ProteinDetectionProtocolType }
     *     
     */
    public ProteinDetectionProtocolType getProteinDetectionProtocol() {
        return proteinDetectionProtocol;
    }

    /**
     * Sets the value of the proteinDetectionProtocol property.
     * 
     * @param value
     *     allowed object is
     *     {@link ProteinDetectionProtocolType }
     *     
     */
    public void setProteinDetectionProtocol(ProteinDetectionProtocolType value) {
        this.proteinDetectionProtocol = value;
    }

}