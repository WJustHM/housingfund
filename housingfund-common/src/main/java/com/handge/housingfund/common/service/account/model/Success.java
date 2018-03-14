package com.handge.housingfund.common.service.account.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;

/**
 * Created by tanyi on 2017/7/25.
 */
@XmlRootElement(name = "Success")
@XmlAccessorType(XmlAccessType.FIELD)
public class Success implements Serializable {

    private String id;
    private String state;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    @Override
    public String toString() {
        return "Success{" +
                "id='" + id + '\'' +
                ", state='" + state + '\'' +
                '}';
    }
}
