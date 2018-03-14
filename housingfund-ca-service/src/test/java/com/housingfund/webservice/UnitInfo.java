
package com.housingfund.webservice;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>unitInfo complex type的 Java 类。
 * <p>
 * <p>以下模式片段指定包含在此类中的预期内容。
 * <p>
 * <pre>
 * &lt;complexType name="unitInfo">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="dwdjrq" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="dwgjjzh" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="dwmc" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="dwdz" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="dwzzjgdm" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="dwlxrxm" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="dwlxrzjhm" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="dwlxrdh" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "unitInfo", propOrder = {
        "dwdjrq",
        "dwgjjzh",
        "dwmc",
        "dwdz",
        "dwzzjgdm",
        "dwlxrxm",
        "dwlxrzjhm",
        "dwlxrdh"
})
public class UnitInfo {

    protected String dwdjrq;
    protected String dwgjjzh;
    protected String dwmc;
    protected String dwdz;
    protected String dwzzjgdm;
    protected String dwlxrxm;
    protected String dwlxrzjhm;
    protected String dwlxrdh;

    /**
     * 获取dwdjrq属性的值。
     *
     * @return possible object is
     * {@link String }
     */
    public String getDwdjrq() {
        return dwdjrq;
    }

    /**
     * 设置dwdjrq属性的值。
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setDwdjrq(String value) {
        this.dwdjrq = value;
    }

    /**
     * 获取dwgjjzh属性的值。
     *
     * @return possible object is
     * {@link String }
     */
    public String getDwgjjzh() {
        return dwgjjzh;
    }

    /**
     * 设置dwgjjzh属性的值。
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setDwgjjzh(String value) {
        this.dwgjjzh = value;
    }

    /**
     * 获取dwmc属性的值。
     *
     * @return possible object is
     * {@link String }
     */
    public String getDwmc() {
        return dwmc;
    }

    /**
     * 设置dwmc属性的值。
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setDwmc(String value) {
        this.dwmc = value;
    }

    /**
     * 获取dwdz属性的值。
     *
     * @return possible object is
     * {@link String }
     */
    public String getDwdz() {
        return dwdz;
    }

    /**
     * 设置dwdz属性的值。
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setDwdz(String value) {
        this.dwdz = value;
    }

    /**
     * 获取dwzzjgdm属性的值。
     *
     * @return possible object is
     * {@link String }
     */
    public String getDwzzjgdm() {
        return dwzzjgdm;
    }

    /**
     * 设置dwzzjgdm属性的值。
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setDwzzjgdm(String value) {
        this.dwzzjgdm = value;
    }

    /**
     * 获取dwlxrxm属性的值。
     *
     * @return possible object is
     * {@link String }
     */
    public String getDwlxrxm() {
        return dwlxrxm;
    }

    /**
     * 设置dwlxrxm属性的值。
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setDwlxrxm(String value) {
        this.dwlxrxm = value;
    }

    /**
     * 获取dwlxrzjhm属性的值。
     *
     * @return possible object is
     * {@link String }
     */
    public String getDwlxrzjhm() {
        return dwlxrzjhm;
    }

    /**
     * 设置dwlxrzjhm属性的值。
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setDwlxrzjhm(String value) {
        this.dwlxrzjhm = value;
    }

    /**
     * 获取dwlxrdh属性的值。
     *
     * @return possible object is
     * {@link String }
     */
    public String getDwlxrdh() {
        return dwlxrdh;
    }

    /**
     * 设置dwlxrdh属性的值。
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setDwlxrdh(String value) {
        this.dwlxrdh = value;
    }

    @Override
    public String toString() {
        return "UnitInfo{" +
                "dwdjrq='" + dwdjrq + '\'' +
                ", dwgjjzh='" + dwgjjzh + '\'' +
                ", dwmc='" + dwmc + '\'' +
                ", dwdz='" + dwdz + '\'' +
                ", dwzzjgdm='" + dwzzjgdm + '\'' +
                ", dwlxrxm='" + dwlxrxm + '\'' +
                ", dwlxrzjhm='" + dwlxrzjhm + '\'' +
                ", dwlxrdh='" + dwlxrdh + '\'' +
                '}';
    }
}
