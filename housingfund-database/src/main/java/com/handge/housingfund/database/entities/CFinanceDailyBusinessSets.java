package com.handge.housingfund.database.entities;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * Created by tanyi on 2017/8/23.
 */
@Entity
@Table(name = "c_finance_daily_business_sets")
@org.hibernate.annotations.Table(appliesTo = "c_finance_daily_business_sets", comment = "日常业务管理表")
public class CFinanceDailyBusinessSets extends Common implements Serializable {

    private static final long serialVersionUID = -6476520444759889297L;
    @Column(name = "YWMC", nullable = false, columnDefinition = "VARCHAR(20) NOT NULL COMMENT '业务名称'")
    private String ywmc;
    @Column(name = "SFMR", columnDefinition = "BIT(1) DEFAULT 1 COMMENT '是否默认'")
    private Boolean sfmr = false;
    @Column(name = "SFSY", columnDefinition = "BIT(1) DEFAULT 0 COMMENT '是否使用'")
    private Boolean sfsy = false;
    @Column(name = "SFZD", columnDefinition = "BIT(1) DEFAULT 1 COMMENT '是否自动'")
    private Boolean sfzd = false;
    @Column(name = "CJR", columnDefinition = "VARCHAR(120) DEFAULT NULL COMMENT '创建人'")
    private String cjr;
    @Column(name = "CJSJ", columnDefinition = "DATETIME DEFAULT NULL COMMENT '创建时间'")
    private Date cjsj;

    @Column(name = "YWBH", columnDefinition = "VARCHAR(5) DEFAULT NULL COMMENT '业务编号'")
    private String ywbh;

    @Column(name = "SFJSPT", columnDefinition = "BIT(1) DEFAULT 0 COMMENT '是否走结算平台'")
    private Boolean sfjspt = false;


    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    // @JoinColumn(name = "cFinanceDailyBusinessClassfiySets", columnDefinition
    // = "varchar(32) DEFAULT NULL COMMENT '所属分类'")
    private CFinanceDailyBusinessClassfiySets cFinanceDailyBusinessClassfiySets;

    public CFinanceDailyBusinessSets() {
        super();

    }

    public CFinanceDailyBusinessSets(String id, Date created_at, Date updated_at, Date deleted_at, boolean deleted,
                                     String ywmc, Boolean sfmr, CFinanceDailyBusinessClassfiySets cFinanceDailyBusinessClassfiySets, String cjr,
                                     Date cjsj, Boolean sfsy, Boolean sfjspt, Boolean sfzd,String ywbh) {
        this.id = id;
        this.created_at = created_at;
        this.updated_at = updated_at;
        this.deleted_at = deleted_at;
        this.deleted = deleted;
        this.ywmc = ywmc;
        this.sfmr = sfmr;
        this.cjr = cjr;
        this.cjsj = cjsj;
        this.sfsy = sfsy;
        this.sfzd = sfzd;
        this.sfjspt = sfjspt;
        this.cFinanceDailyBusinessClassfiySets = cFinanceDailyBusinessClassfiySets;
        this.ywbh = ywbh;
    }

    public String getYwbh() {
        return ywbh;
    }

    public void setYwbh(String ywbh) {
        this.ywbh = ywbh;
    }

    public Boolean getSfjspt() {
        return sfjspt;
    }

    public void setSfjspt(Boolean sfjspt) {
        this.updated_at = new Date();
        this.sfjspt = sfjspt;
    }

    public Boolean getSfsy() {
        return sfsy;
    }

    public void setSfsy(Boolean sfsy) {
        this.updated_at = new Date();
        this.sfsy = sfsy;
    }

    public Boolean getSfzd() {
        return sfzd;
    }

    public void setSfzd(Boolean sfzd) {
        this.updated_at = new Date();
        this.sfzd = sfzd;
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

    public CFinanceDailyBusinessClassfiySets getcFinanceDailyBusinessClassfiySets() {
        return cFinanceDailyBusinessClassfiySets;
    }

    public void setcFinanceDailyBusinessClassfiySets(
            CFinanceDailyBusinessClassfiySets cFinanceDailyBusinessClassfiySets) {
        this.updated_at = new Date();
        this.cFinanceDailyBusinessClassfiySets = cFinanceDailyBusinessClassfiySets;
    }

    public void removeClassify() {
        this.cFinanceDailyBusinessClassfiySets.getcFinanceDailyBusinessSetsList().remove(this);
        this.setcFinanceDailyBusinessClassfiySets(null);
    }
}
