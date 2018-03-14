package com.handge.housingfund.database.entities;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * Created by tanyi on 2017/7/15.
 */
@Entity
@Table(name = "c_account_department")
@org.hibernate.annotations.Table(appliesTo = "c_account_department", comment = "组织管理-部门表")
public class CAccountDepartment extends Common implements Serializable {

    private static final long serialVersionUID = -5732437765719669492L;

    @Column(name = "MingCheng", nullable = false, columnDefinition = "VARCHAR(255) NOT NULL COMMENT '名称'")
    private String MingCheng;

    @Column(name = "FZR", nullable = false, columnDefinition = "VARCHAR(120) NOT NULL COMMENT '负责人'")
    private String FZR;

    @Column(name = "LXDH", columnDefinition = "VARCHAR(20) DEFAULT NULL COMMENT '联系电话'")
    private String LXDH;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "cAccountDepartment")
    private List<CAccountEmployee> cAccountEmployees;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private CAccountNetwork cAccountNetwork;

    public String getMingCheng() {
        return MingCheng;
    }

    public void setMingCheng(String mingCheng) {
        this.updated_at = new Date();
        MingCheng = mingCheng;
    }

    public List<CAccountEmployee> getcAccountEmployees() {
        return cAccountEmployees;
    }

    public void setcAccountEmployees(List<CAccountEmployee> cAccountEmployees) {
        this.updated_at = new Date();
        this.cAccountEmployees = cAccountEmployees;

    }

    public CAccountNetwork getcAccountNetwork() {
        return cAccountNetwork;
    }

    public void setcAccountNetwork(CAccountNetwork cAccountNetwork) {
        this.updated_at = new Date();
        this.cAccountNetwork = cAccountNetwork;
    }

    public String getFZR() {
        return FZR;
    }

    public void setFZR(String FZR) {
        this.updated_at = new Date();
        this.FZR = FZR;
    }

    public String getLXDH() {
        return LXDH;
    }

    public void setLXDH(String LXDH) {
        this.updated_at = new Date();
        this.LXDH = LXDH;
    }

    public CAccountDepartment() {
        super();


    }

    public CAccountDepartment(String id, Date created_at, boolean deleted, Date deleted_at, Date updated_at,
                              String mingCheng, String FZR, String LXDH, List<CAccountEmployee> cAccountEmployees) {
        this.id = id;
        this.created_at = created_at;
        this.deleted = deleted;
        this.deleted_at = deleted_at;
        this.updated_at = updated_at;
        this.MingCheng = mingCheng;
        this.FZR = FZR;
        this.LXDH = LXDH;
        this.cAccountEmployees = cAccountEmployees;
    }

}