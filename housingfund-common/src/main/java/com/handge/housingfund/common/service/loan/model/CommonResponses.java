package com.handge.housingfund.common.service.loan.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;


@XmlRootElement(name = "CommonResponses")
@XmlAccessorType(XmlAccessType.FIELD)
public class CommonResponses  implements Serializable {

    private String State;  //操作状态

    private String Id;  //id(如：若是没有流水号id,那它就是各模块的模块id)

    public String getState() {

        return this.State;

    }


    public void setState(String State) {

        this.State = State;

    }


    public String getId() {

        return this.Id;

    }


    public void setId(String Id) {

        this.Id = Id;

    }


    public String toString() {

        return "CommonResponses{" +

                "State='" + this.State + '\'' + "," +
                "Id='" + this.Id + '\'' +

                "}";

    }
}