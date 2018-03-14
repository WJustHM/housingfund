package com.handge.housingfund.common.service.collection.model.individual;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;

/**
 * Created by sjw on 2017/10/25.
 */
@XmlRootElement(name = "ImportExcelErrorListRes")
@XmlAccessorType(XmlAccessType.FIELD)
public class ImportExcelErrorListRes implements Serializable {

    private static final long serialVersionUID = -7851940459520026512L;

    private String name; //名称

    private String mes; //消息

    private String status; //状态

    public String getName() {
        return name;
    }


    public void setName(String name) {
        this.name = name;
    }

    public String getMes() {
        return mes;
    }

    public void setMes(String mes) {
        this.mes = mes;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
