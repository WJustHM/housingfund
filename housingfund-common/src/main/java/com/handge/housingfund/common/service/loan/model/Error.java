package com.handge.housingfund.common.service.loan.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;


@XmlRootElement(name = "Error")
@XmlAccessorType(XmlAccessType.FIELD)
public class Error  implements Serializable {

    private static final long serialVersionUID = -345244049455007876L;
    private String Msg;  //操作信息

    private String Code;  //操作码

    public String getMsg() {

        return this.Msg;

    }


    public void setMsg(String Msg) {

        this.Msg = Msg;

    }


    public String getCode() {

        return this.Code;

    }


    public void setCode(String Code) {

        this.Code = Code;

    }


    public String toString() {

        return "Error{" +

                "Msg='" + this.Msg + '\'' + "," +
                "Code='" + this.Code + '\'' +

                "}";

    }
}