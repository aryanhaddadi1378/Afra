//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vhudson-jaxb-ri-2.2-146 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2019.08.23 at 10:20:04 AM IRDT 
//


package org.rebecalang.compiler.modelcompiler.corerebeca.objectmodel;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for VariableDeclarator complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="VariableDeclarator">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="variableName" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="variableInitializer" type="{http://rebecalang.org/compiler/modelcompiler/corerebeca}VariableInitializer"/>
 *       &lt;/sequence>
 *       &lt;attribute name="lineNumber" type="{http://www.w3.org/2001/XMLSchema}int" />
 *       &lt;attribute name="character" type="{http://www.w3.org/2001/XMLSchema}int" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "VariableDeclarator", propOrder = {
    "variableName",
    "variableInitializer"
})
public class VariableDeclarator {

    @XmlElement(required = true)
    protected String variableName;
    @XmlElement(required = true)
    protected VariableInitializer variableInitializer;
    @XmlAttribute(name = "lineNumber")
    protected Integer lineNumber;
    @XmlAttribute(name = "character")
    protected Integer character;

    /**
     * Gets the value of the variableName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getVariableName() {
        return variableName;
    }

    /**
     * Sets the value of the variableName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setVariableName(String value) {
        this.variableName = value;
    }

    /**
     * Gets the value of the variableInitializer property.
     * 
     * @return
     *     possible object is
     *     {@link VariableInitializer }
     *     
     */
    public VariableInitializer getVariableInitializer() {
        return variableInitializer;
    }

    /**
     * Sets the value of the variableInitializer property.
     * 
     * @param value
     *     allowed object is
     *     {@link VariableInitializer }
     *     
     */
    public void setVariableInitializer(VariableInitializer value) {
        this.variableInitializer = value;
    }

    /**
     * Gets the value of the lineNumber property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getLineNumber() {
        return lineNumber;
    }

    /**
     * Sets the value of the lineNumber property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setLineNumber(Integer value) {
        this.lineNumber = value;
    }

    /**
     * Gets the value of the character property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getCharacter() {
        return character;
    }

    /**
     * Sets the value of the character property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setCharacter(Integer value) {
        this.character = value;
    }

}
