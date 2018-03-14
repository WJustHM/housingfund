package com.handge.housingfund.common.service.account.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;

/**
 * Created by gxy on 17-7-17.
 */
@XmlRootElement(name = "DepartmentMember")
@XmlAccessorType(XmlAccessType.FIELD)
public class DepartmentMember implements Serializable {

    private String id;
    private String name;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "DepartmentMember{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
}
