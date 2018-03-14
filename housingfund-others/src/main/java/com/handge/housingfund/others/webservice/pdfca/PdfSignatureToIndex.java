
package com.handge.housingfund.others.webservice.pdfca;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import java.util.ArrayList;
import java.util.List;


/**
 * <p>pdfSignatureToIndex complex type的 Java 类。
 * 
 * <p>以下模式片段指定包含在此类中的预期内容。
 * 
 * <pre>
 * &lt;complexType name="pdfSignatureToIndex">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="base64File" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0" form="qualified"/>
 *         &lt;element name="fileName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0" form="qualified"/>
 *         &lt;element name="stampType" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0" form="qualified"/>
 *         &lt;element name="stampPattern" type="{http://ws.sign.szca.com.cn/}stampPattern" maxOccurs="unbounded" minOccurs="0" form="qualified"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "pdfSignatureToIndex", propOrder = {
    "base64File",
    "fileName",
    "stampType",
    "stampPattern"
})
public class PdfSignatureToIndex {

    @XmlElement(namespace = "http://ws.sign.szca.com.cn/")
    protected String base64File;
    @XmlElement(namespace = "http://ws.sign.szca.com.cn/")
    protected String fileName;
    @XmlElement(namespace = "http://ws.sign.szca.com.cn/")
    protected String stampType;
    @XmlElement(namespace = "http://ws.sign.szca.com.cn/")
    protected List<StampPattern> stampPattern;

    /**
     * 获取base64File属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getBase64File() {
        return base64File;
    }

    /**
     * 设置base64File属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setBase64File(String value) {
        this.base64File = value;
    }

    /**
     * 获取fileName属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFileName() {
        return fileName;
    }

    /**
     * 设置fileName属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFileName(String value) {
        this.fileName = value;
    }

    /**
     * 获取stampType属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getStampType() {
        return stampType;
    }

    /**
     * 设置stampType属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setStampType(String value) {
        this.stampType = value;
    }

    /**
     * Gets the value of the stampPattern property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the stampPattern property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getStampPattern().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link StampPattern }
     * 
     * 
     */
    public List<StampPattern> getStampPattern() {
        if (stampPattern == null) {
            stampPattern = new ArrayList<StampPattern>();
        }
        return this.stampPattern;
    }

}
