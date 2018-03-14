package com.handge.housingfund.common.service.account.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;

/**
 * Created by tanyi on 2017/7/18.
 */
@XmlRootElement(name = "Role")
@XmlAccessorType(XmlAccessType.FIELD)
public class Role implements Serializable {

    private String role_name;//角色名称
    private String role_note;//角色描述

    public String getRole_name() {
        return role_name;
    }

    public void setRole_name(String role_name) {
        this.role_name = role_name;
    }

    public String getRole_note() {
        return role_note;
    }

    public void setRole_note(String role_note) {
        this.role_note = role_note;
    }
}
