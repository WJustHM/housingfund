package com.handge.housingfund.common.service.account.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;

/**
 * Created by gxy on 17-7-20.
 */
@XmlRootElement(name = "AuthWithDepartment")
@XmlAccessorType(XmlAccessType.FIELD)
public class AuthWithDepartment extends Auth implements Serializable {
    private String departmentName;

    public String getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }
}
