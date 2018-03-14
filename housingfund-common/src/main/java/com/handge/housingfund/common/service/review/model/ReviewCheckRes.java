package com.handge.housingfund.common.service.review.model;


import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;

/**
 * 检查是否属于审核中业务的返回结果
 */
@XmlRootElement(name = "ReviewCheckRes")
@XmlAccessorType(XmlAccessType.FIELD)
public class ReviewCheckRes implements Serializable{

    private String code;  //返回状态；若成功是success，若失败fail；

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }


    @Override
    public String toString() {
        return "ReviewCheckRes{" +
                "code='" + code + '\'' +
                '}';
    }
}
