
package com.handge.housingfund.others.webservice.pdfca;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>stamp complex type的 Java 类。
 * 
 * <p>以下模式片段指定包含在此类中的预期内容。
 * 
 * <pre>
 * &lt;complexType name="stamp">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="addTag" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *         &lt;element name="bottom" type="{http://www.w3.org/2001/XMLSchema}float"/>
 *         &lt;element name="createDate" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="left" type="{http://www.w3.org/2001/XMLSchema}float"/>
 *         &lt;element name="pageNum" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="signerId" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "stamp", propOrder = {
    "addTag",
    "bottom",
    "createDate",
    "left",
    "pageNum",
    "signerId"
})
public class Stamp {

    protected boolean addTag;
    protected float bottom;
    protected String createDate;
    protected float left;
    protected int pageNum;
    protected String signerId;

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
     * 获取bottom属性的值。
     * 
     */
    public float getBottom() {
        return bottom;
    }

    /**
     * 设置bottom属性的值。
     * 
     */
    public void setBottom(float value) {
        this.bottom = value;
    }

    /**
     * 获取createDate属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCreateDate() {
        return createDate;
    }

    /**
     * 设置createDate属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCreateDate(String value) {
        this.createDate = value;
    }

    /**
     * 获取left属性的值。
     * 
     */
    public float getLeft() {
        return left;
    }

    /**
     * 设置left属性的值。
     * 
     */
    public void setLeft(float value) {
        this.left = value;
    }

    /**
     * 获取pageNum属性的值。
     * 
     */
    public int getPageNum() {
        return pageNum;
    }

    /**
     * 设置pageNum属性的值。
     * 
     */
    public void setPageNum(int value) {
        this.pageNum = value;
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

}
