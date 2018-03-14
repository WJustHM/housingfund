//
// 此文件是由 JavaTM Architecture for XML Binding (JAXB) 引用实现 v2.2.8-b130911.1802 生成的
// 请访问 <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// 在重新编译源模式时, 对此文件的所有修改都将丢失。
// 生成时间: 2017.12.07 时间 12:40:38 PM CST 
//


package com.handge.housingfund.common.service.bank.xmlbean;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElements;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.NormalizedStringAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;


/**
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "fieldListOrFieldXxxxOrField"
})
@XmlRootElement(name = "field-list")
public class FieldList {

    @XmlAttribute(name = "name", required = true)
    @XmlJavaTypeAdapter(NormalizedStringAdapter.class)
    protected String name;
    @XmlElements({
        @XmlElement(name = "field-list", required = true, type = FieldList.class),
        @XmlElement(name = "field-xxxx", required = true, type = FieldXxxx.class),
        @XmlElement(name = "field", required = true, type = Field.class)
    })
    protected List<Object> fieldListOrFieldXxxxOrField;

    /**
     * 获取name属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getName() {
        return name;
    }

    /**
     * 设置name属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setName(String value) {
        this.name = value;
    }

    /**
     * Gets the value of the fieldListOrFieldXxxxOrField property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the fieldListOrFieldXxxxOrField property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getFieldListOrFieldXxxxOrField().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link FieldList }
     * {@link FieldXxxx }
     * {@link Field }
     * 
     * 
     */
    public List<Object> getFieldListOrFieldXxxxOrField() {
        if (fieldListOrFieldXxxxOrField == null) {
            fieldListOrFieldXxxxOrField = new ArrayList<Object>();
        }
        return this.fieldListOrFieldXxxxOrField;
    }

}
