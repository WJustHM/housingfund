package com.handge.housingfund.common.service.account.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;

/**
 * Created by gxy on 17-7-17.
 */
@XmlRootElement(name = "ReponseMsg")
@XmlAccessorType(XmlAccessType.FIELD)
public class ResponseMsg implements Serializable {
    private String status;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "ReponseMsg{" +
                "status='" + status + '\'' +
                '}';
    }
}
