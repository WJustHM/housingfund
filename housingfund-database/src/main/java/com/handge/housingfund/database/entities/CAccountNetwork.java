package com.handge.housingfund.database.entities;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * Created by tanyi on 2017/7/15.
 */
@Entity
@Table(name = "c_account_network")
@org.hibernate.annotations.Table(appliesTo = "c_account_network", comment = "组织管理-网点表")
public class CAccountNetwork extends Common implements Serializable {

    private static final long serialVersionUID = -6571795255591327724L;

    @Column(name = "MingCheng", nullable = false, columnDefinition = "VARCHAR(255) NOT NULL COMMENT '名称'")
    private String MingCheng;

    @Column(name = "BLSJ", nullable = false, columnDefinition = "VARCHAR(255) NOT NULL COMMENT '办理时间'")
    private String BLSJ;

    @Column(name = "FZR", nullable = false, columnDefinition = "VARCHAR(120) NOT NULL COMMENT '负责人'")
    private String FZR;

    @Column(name = "LXDH", nullable = false, columnDefinition = "VARCHAR(20) NOT NULL COMMENT '联系电话'")
    private String LXDH;

    @Column(name = "DiQu", nullable = false, columnDefinition = "VARCHAR(255) NOT NULL COMMENT '地区'")
    private String DiQu;

    @Column(name = "XXDZ", nullable = false, columnDefinition = "VARCHAR(255) NOT NULL COMMENT '详细地址'")
    private String XXDZ;

    @Column(name = "SJWD", columnDefinition = "VARCHAR(32) DEFAULT NULL COMMENT '上级网点ID'")
    private String SJWD;

    @Column(name = "JingDu", columnDefinition = "VARCHAR(255) DEFAULT NULL COMMENT '经度'")
    private String JingDu;

    @Column(name = "WeiDu", columnDefinition = "VARCHAR(255) DEFAULT NULL COMMENT '维度'")
    private String WeiDu;

    @OneToMany(cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH}, fetch = FetchType.LAZY, mappedBy = "cAccountNetwork")
    private List<CAccountDepartment> cAccountDepartments;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "cAccountNetwork")
    private List<CAccountEmployee> cAccountEmployees;

    public String getMingCheng() {
        return MingCheng;
    }

    public void setMingCheng(String mingCheng) {
        this.updated_at = new Date();
        MingCheng = mingCheng;
    }

    public String getBLSJ() {
        return BLSJ;
    }

    public void setBLSJ(String BLSJ) {
        this.updated_at = new Date();
        this.BLSJ = BLSJ;
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

    public String getDiQu() {
        return DiQu;
    }

    public void setDiQu(String diQu) {
        this.updated_at = new Date();
        DiQu = diQu;
    }

    public String getXXDZ() {
        return XXDZ;
    }

    public void setXXDZ(String XXDZ) {
        this.updated_at = new Date();
        this.XXDZ = XXDZ;
    }

    public String getSJWD() {
        return SJWD;
    }

    public void setSJWD(String SJWD) {
        this.updated_at = new Date();
        this.SJWD = SJWD;
    }

    public String getJingDu() {
        return JingDu;
    }

    public void setJingDu(String jingDu) {
        this.updated_at = new Date();
        JingDu = jingDu;
    }

    public String getWeiDu() {
        return WeiDu;
    }

    public void setWeiDu(String weiDu) {
        this.updated_at = new Date();
        WeiDu = weiDu;
    }

    public List<CAccountDepartment> getcAccountDepartments() {
        return cAccountDepartments;
    }

    public void setcAccountDepartments(List<CAccountDepartment> cAccountDepartments) {
        this.updated_at = new Date();
        this.cAccountDepartments = cAccountDepartments;
    }

    public List<CAccountEmployee> getcAccountEmployees() {
        return cAccountEmployees;
    }

    public void setcAccountEmployees(List<CAccountEmployee> cAccountEmployees) {
        this.updated_at = new Date();
        this.cAccountEmployees = cAccountEmployees;
    }

    public CAccountNetwork() {
        super();

    }

    public CAccountNetwork(String id, Date created_at, boolean deleted, Date deleted_at, Date updated_at,
                           String mingCheng, String BLSJ, String FZR, String LXDH, String diQu, String XXDZ, String SJWD, String jingDu,
                           String weiDu, List<CAccountDepartment> cAccountDepartments, List<CAccountEmployee> cAccountEmployees) {
        this.id = id;
        this.created_at = created_at;
        this.deleted = deleted;
        this.deleted_at = deleted_at;
        this.updated_at = updated_at;
        this.MingCheng = mingCheng;
        this.BLSJ = BLSJ;
        this.FZR = FZR;
        this.LXDH = LXDH;
        this.DiQu = diQu;
        this.XXDZ = XXDZ;
        this.SJWD = SJWD;
        this.JingDu = jingDu;
        this.WeiDu = weiDu;
        this.cAccountDepartments = cAccountDepartments;
        this.cAccountEmployees = cAccountEmployees;
    }

}