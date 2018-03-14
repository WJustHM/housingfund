package com.handge.housingfund.common.service.account.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;


@XmlRootElement(name = "Msg")
@XmlAccessorType(XmlAccessType.FIELD)
public class Msg implements Serializable {

    private Integer Code;  //

    private String Msg;  //

    public Integer getCode() {

        return this.Code;

    }


    public void setCode(Integer Code) {

        this.Code = Code;

    }


    public String getMsg() {

        return this.Msg;

    }


    public void setMsg(String Msg) {

        this.Msg = Msg;

    }


    public String toString() {

        return "com.handge.housingfund.collection.model.RpcMsg{" +

                "Code='" + this.Code + '\'' + "," +
                "RpcMsg='" + this.Msg + '\'' +

                "}+";

    }
}