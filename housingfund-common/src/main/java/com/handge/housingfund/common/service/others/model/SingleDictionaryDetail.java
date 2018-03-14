package com.handge.housingfund.common.service.others.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Created by sunjianwen on 2017/9/13.
 */
@XmlRootElement(name = "SingleDictionaryDetail")
@XmlAccessorType(XmlAccessType.FIELD)
public class SingleDictionaryDetail implements Serializable {
    private BigDecimal no;
    private String code;
    private String type;
    private String name;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public BigDecimal getNo() {
        return no;
    }

    public void setNo(BigDecimal no) {
        this.no = no;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "SingleDictionaryDetail{" + "no='" + this.no + '\'' + ", name=" + this.name + ", code=" + this.code
                + ", type=" + this.type + "}";
    }
}
