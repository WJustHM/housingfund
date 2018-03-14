package com.handge.housingfund.common.service.account.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by gxy on 17-7-19.
 */
@XmlRootElement(name = "RoleWithAuth")
@XmlAccessorType(XmlAccessType.FIELD)
public class RoleWithAuth implements Serializable {
    private RoleRes role;
    private List<Auth> auths = new ArrayList<>();
    private List<RolePermission> rolePermissions = new ArrayList<>();

    public RoleRes getRole() {
        return role;
    }

    public void setRole(RoleRes role) {
        this.role = role;
    }

    public List<Auth> getAuths() {
        return auths;
    }

    public void setAuths(List<Auth> auths) {
        this.auths = auths;
    }

    public List<RolePermission> getRolePermissions() {
        return rolePermissions;
    }

    public void setRolePermissions(List<RolePermission> rolePermissions) {
        this.rolePermissions = rolePermissions;
    }
}
