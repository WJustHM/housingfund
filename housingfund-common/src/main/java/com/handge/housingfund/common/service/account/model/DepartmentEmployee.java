package com.handge.housingfund.common.service.account.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;

/**
 * Created by tanyi on 2017/7/17.
 */
@XmlRootElement(name = "DepartmentEmployee")
@XmlAccessorType(XmlAccessType.FIELD)
public class DepartmentEmployee implements Serializable {

    private String id;//id
//    private String xingming;//姓名
//    private String wangdian_id;//网点ID
//    private String bumen_id;//部门ID

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
//
//    public String getXingming() {
//        return xingming;
//    }
//
//    public void setXingming(String xingming) {
//        this.xingming = xingming;
//    }
//
//    public String getWangdian_id() {
//        return wangdian_id;
//    }
//
//    public void setWangdian_id(String wangdian_id) {
//        this.wangdian_id = wangdian_id;
//    }
//
//    public String getBumen_id() {
//        return bumen_id;
//    }
//
//    public void setBumen_id(String bumen_id) {
//        this.bumen_id = bumen_id;
//    }
}
