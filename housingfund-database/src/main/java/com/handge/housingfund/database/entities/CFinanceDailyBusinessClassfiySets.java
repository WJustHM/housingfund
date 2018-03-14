package com.handge.housingfund.database.entities;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * Created by tanyi on 2017/8/23.
 */
@Entity
@Table(name = "c_finance_daily_business_classfiy_sets")
@org.hibernate.annotations.Table(appliesTo = "c_finance_daily_business_classfiy_sets", comment = "日常业务分类管理表")
public class CFinanceDailyBusinessClassfiySets extends Common implements Serializable {

    private static final long serialVersionUID = 1308452198316513244L;
    @Column(name = "YWMC", nullable = false, columnDefinition = "VARCHAR(20) NOT NULL COMMENT '分类名称'")
    private String ywmc;
    @Column(name = "SFMR", columnDefinition = "BIT(1) DEFAULT 1 COMMENT '是否默认'")
    private Boolean sfmr;
    @Column(name = "SFRC", columnDefinition = "BIT(1) DEFAULT 0 COMMENT '是否日常'")
    private Boolean sfrc;
    @Column(name = "CJR", columnDefinition = "VARCHAR(120) DEFAULT NULL COMMENT '创建人'")
    private String cjr;
    @Column(name = "CJSJ", columnDefinition = "DATETIME DEFAULT NULL COMMENT '创建时间'")
    private Date cjsj;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "cFinanceDailyBusinessClassfiySets")
    private List<CFinanceDailyBusinessSets> cFinanceDailyBusinessSetsList;

    public CFinanceDailyBusinessClassfiySets() {
        super();

    }

    public CFinanceDailyBusinessClassfiySets(String id, Date created_at, Date updated_at, Date deleted_at,
                                             boolean deleted, String ywmc, Boolean sfmr, List<CFinanceDailyBusinessSets> cFinanceDailyBusinessSetsList,
                                             String cjr, Date cjsj, Boolean sfrc) {
        this.id = id;
        this.created_at = created_at;
        this.updated_at = updated_at;
        this.deleted_at = deleted_at;
        this.deleted = deleted;
        this.ywmc = ywmc;
        this.sfmr = sfmr;
        this.cjr = cjr;
        this.cjsj = cjsj;
        this.sfrc = sfrc;
        this.cFinanceDailyBusinessSetsList = cFinanceDailyBusinessSetsList;
    }

    public Boolean getSfrc() {
        return sfrc;
    }

    public void setSfrc(Boolean sfrc) {
        this.updated_at = new Date();
        this.sfrc = sfrc;
    }

    public String getCjr() {
        return cjr;
    }

    public void setCjr(String cjr) {
        this.updated_at = new Date();
        this.cjr = cjr;
    }

    public Date getCjsj() {
        return cjsj;
    }

    public void setCjsj(Date cjsj) {
        this.updated_at = new Date();
        this.cjsj = cjsj;
    }

    public String getYwmc() {
        return ywmc;
    }

    public void setYwmc(String ywmc) {
        this.updated_at = new Date();
        this.ywmc = ywmc;
    }

    public Boolean getSfmr() {
        return sfmr;
    }

    public void setSfmr(Boolean sfmr) {
        this.updated_at = new Date();
        this.sfmr = sfmr;
    }

    public List<CFinanceDailyBusinessSets> getcFinanceDailyBusinessSetsList() {
        return cFinanceDailyBusinessSetsList;
    }

    public void setcFinanceDailyBusinessSetsList(List<CFinanceDailyBusinessSets> cFinanceDailyBusinessSetsList) {
        this.updated_at = new Date();
        this.cFinanceDailyBusinessSetsList = cFinanceDailyBusinessSetsList;
    }
}
