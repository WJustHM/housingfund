package com.handge.housingfund.common.service.collection.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;

/**
 * Created by 凡 on 2017/10/7.
 */
@XmlRootElement(name = "BusCommonRetrun")
@XmlAccessorType(XmlAccessType.FIELD)
public class BusCommonRetrun implements Serializable {

    private String Status;  //状态

    private String YWLSH;  //业务流水号

    private String Message; //返回消息

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }

    public String getYWLSH() {
        return YWLSH;
    }

    public void setYWLSH(String YWLSH) {
        this.YWLSH = YWLSH;
    }

    public String getMessage() {
        return Message;
    }

    public void setMessage(String message) {
        Message = message;
    }

    public BusCommonRetrun() {
    }

    public BusCommonRetrun(String status, String YWLSH) {
        Status = status;
        this.YWLSH = YWLSH;
    }

    @Override
    public String toString() {
        return "BusCommonRetrun{" +
                "Status='" + Status + '\'' +
                ", YWLSH='" + YWLSH + '\'' +
                ", Message='" + Message + '\'' +
                '}';
    }
}
