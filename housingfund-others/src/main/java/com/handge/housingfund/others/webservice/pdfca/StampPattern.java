
package com.handge.housingfund.others.webservice.pdfca;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>stampPattern complex type的 Java 类。
 * 
 * <p>以下模式片段指定包含在此类中的预期内容。
 * 
 * <pre>
 * &lt;complexType name="stampPattern">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="addTag" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *         &lt;element name="endPage" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="indexName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="rightSeal" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *         &lt;element name="signerId" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="stampType" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="startPage" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "stampPattern", propOrder = {
    "addTag",
    "endPage",
    "indexName",
    "rightSeal",
    "signerId",
    "stampType",
    "startPage"
})
public class StampPattern {

    protected boolean addTag;
    protected int endPage;
    protected String indexName;
    protected boolean rightSeal;
    protected String signerId;
    protected String stampType;
    protected int startPage;

    /**
     * 获取addTag属性的值。
     * 
     */
    public boolean isAddTag() {
        return addTag;
    }

    /**
     * 设置addTag属性的值。
     * 
     */
    public void setAddTag(boolean value) {
        this.addTag = value;
    }

    /**
     * 获取endPage属性的值。
     * 
     */
    public int getEndPage() {
        return endPage;
    }

    /**
     * 设置endPage属性的值。
     * 
     */
    public void setEndPage(int value) {
        this.endPage = value;
    }

    /**
     * 获取indexName属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIndexName() {
        return indexName;
    }

    /**
     * 设置indexName属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIndexName(String value) {
        this.indexName = value;
    }

    /**
     * 获取rightSeal属性的值。
     * 
     */
    public boolean isRightSeal() {
        return rightSeal;
    }

    /**
     * 设置rightSeal属性的值。
     * 
     */
    public void setRightSeal(boolean value) {
        this.rightSeal = value;
    }

    /**
     * 获取signerId属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSignerId() {
        return signerId;
    }

    /**
     * 设置signerId属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSignerId(String value) {
        this.signerId = value;
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
     * 获取startPage属性的值。
     * 
     */
    public int getStartPage() {
        return startPage;
    }

    /**
     * 设置startPage属性的值。
     * 
     */
    public void setStartPage(int value) {
        this.startPage = value;
    }

}
