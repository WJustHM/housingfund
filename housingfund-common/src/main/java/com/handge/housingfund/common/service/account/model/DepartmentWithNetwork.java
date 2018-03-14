package com.handge.housingfund.common.service.account.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * Created by tanyi on 2017/7/31.
 */
@XmlRootElement(name = "DepartmentWithNetwork")
@XmlAccessorType(XmlAccessType.FIELD)
public class DepartmentWithNetwork implements Serializable {
    protected String id;
    protected Date created_at;
    protected Date updated_at;
    protected Date deleted_at;
    protected boolean deleted;

    private String MingCheng;//名称

    private String FZR;//负责人

    private String LXDH;//联系电话

    private List<Employee> cAccountEmployees;

    private Network cAccountNetwork;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Date getCreated_at() {
        return created_at;
    }

    public void setCreated_at(Date created_at) {
        this.created_at = created_at;
    }

    public Date getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(Date updated_at) {
        this.updated_at = updated_at;
    }

    public Date getDeleted_at() {
        return deleted_at;
    }

    public void setDeleted_at(Date deleted_at) {
        this.deleted_at = deleted_at;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    public String getMingCheng() {
        return MingCheng;
    }

    public void setMingCheng(String mingCheng) {
        MingCheng = mingCheng;
    }

    public String getFZR() {
        return FZR;
    }

    public void setFZR(String FZR) {
        this.FZR = FZR;
    }

    public String getLXDH() {
        return LXDH;
    }

    public void setLXDH(String LXDH) {
        this.LXDH = LXDH;
    }

    public List<Employee> getcAccountEmployees() {
        return cAccountEmployees;
    }

    public void setcAccountEmployees(List<Employee> cAccountEmployees) {
        this.cAccountEmployees = cAccountEmployees;
    }

    public Network getcAccountNetwork() {
        return cAccountNetwork;
    }

    public void setcAccountNetwork(Network cAccountNetwork) {
        this.cAccountNetwork = cAccountNetwork;
    }
}
