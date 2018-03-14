package com.handge.housingfund.database.entities;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by tanyi on 2017/8/23.
 */
@Entity
@Table(name = "c_finance_business_process", indexes = {
        @Index(name = "INDEX_CWYWLSH", columnList = "YWLSH", unique = true)})
@org.hibernate.annotations.Table(appliesTo = "c_finance_business_process", comment = "财务-业务流程表")
public class CFinanceBusinessProcess extends Common implements java.io.Serializable {

    private static final long serialVersionUID = -9150903255094631520L;
    @Column(name = "CZY", columnDefinition = "VARCHAR(120) DEFAULT NULL COMMENT '操作员'")
    private String czy;
    @Column(name = "YWWD", columnDefinition = "VARCHAR(255) DEFAULT NULL COMMENT '业务网点'")
    private String ywwd;
    @Column(name = "CZNR", columnDefinition = "VARCHAR(255) DEFAULT NULL COMMENT '操作内容'")
    private String cznr;
    @Column(name = "CZQD", columnDefinition = "VARCHAR(255) DEFAULT NULL COMMENT '操作渠道'")
    private String czqd;
    @Column(name = "BLSJ", columnDefinition = "DATETIME DEFAULT NULL COMMENT '办理时间'")
    private Date blsj;
    @Column(name = "STEP", columnDefinition = "VARCHAR(20) DEFAULT NULL COMMENT '状态机状态'")
    private String step;
    @Column(name = "SBYY", columnDefinition = "TEXT DEFAULT NULL COMMENT '失败原因'")
    private String sbyy;
    @Column(name = "YWLSH", unique = true, columnDefinition = "VARCHAR(20) DEFAULT NULL COMMENT '业务流水号'")
    private String ywlsh;
    @Column(name = "ZHCLRY", columnDefinition = "VARCHAR(120) DEFAULT NULL COMMENT '最后处理人员'")
    private String zhclry;

    @Column(name = "SHSJ", columnDefinition = "DATETIME DEFAULT NULL COMMENT '审核时间'")
    private Date shsj;    //审核时间
    @Column(name = "DDSJ", columnDefinition = "DATETIME DEFAULT NULL COMMENT '到达时间'")
    private Date ddsj;    //到达时间
    @Column(name = "BJSJ", columnDefinition = "DATETIME DEFAULT NULL COMMENT '办结时间'")
    private Date bjsj;    //办结时间
    @Column(name = "SHYBH", columnDefinition = "TEXT DEFAULT NULL COMMENT '审核员编号'")
    private String shybh; //审核员编号

    @Column(name = "JZPZH", columnDefinition = "VARCHAR(20) DEFAULT NULL COMMENT '记账凭证号'")
    private String jzpzh;


    // 日常财务处理
    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "rccwcl")
    private CFinanceDailyBusinessVice cFinanceDailyBusinessVice;

    public CFinanceBusinessProcess() {
        super();

    }

    public CFinanceBusinessProcess(String id, Date created_at, Date updated_at, Date deleted_at, Boolean deleted,
                                   String czy, String ywwd, String cznr, String czqd, Date blsj, String step, String sbyy,
                                   String ywlsh, String zhclry, Date shsj, Date ddsj, Date bjsj, String shybh, String jzpzh,
                                   CFinanceDailyBusinessVice cFinanceDailyBusinessVice) {
        this.id = id;
        this.created_at = created_at;
        this.updated_at = updated_at;
        this.deleted_at = deleted_at;
        this.deleted = deleted;
        this.czy = czy;
        this.ywwd = ywwd;
        this.cznr = cznr;
        this.czqd = czqd;
        this.blsj = blsj;
        this.step = step;
        this.sbyy = sbyy;
        this.ywlsh = ywlsh;
        this.zhclry = zhclry;
        this.shsj = shsj;
        this.ddsj = ddsj;
        this.bjsj = bjsj;
        this.shybh = shybh;
        this.jzpzh = jzpzh;
        this.cFinanceDailyBusinessVice = cFinanceDailyBusinessVice;
    }

    public String getJzpzh() {
        return jzpzh;
    }

    public void setJzpzh(String jzpzh) {
        this.jzpzh = jzpzh;
    }

    public String getSbyy() {
        return sbyy;
    }

    public void setSbyy(String sbyy) {
        this.updated_at = new Date();
        this.sbyy = sbyy;
    }

    public String getCzy() {
        return czy;
    }

    public void setCzy(String czy) {
        this.updated_at = new Date();
        this.czy = czy;
    }

    public Date getShsj() {
        return shsj;
    }

    public void setShsj(Date shsj) {
        this.shsj = shsj;
    }

    public Date getDdsj() {
        return ddsj;
    }

    public void setDdsj(Date ddsj) {
        this.ddsj = ddsj;
    }

    public Date getBjsj() {
        return bjsj;
    }

    public void setBjsj(Date bjsj) {
        this.bjsj = bjsj;
    }

    public String getShybh() {
        return shybh;
    }

    public void setShybh(String shybh) {
        this.shybh = shybh;
    }

    public String getYwwd() {
        return ywwd;
    }

    public void setYwwd(String ywwd) {
        this.updated_at = new Date();
        this.ywwd = ywwd;
    }

    public String getCznr() {
        return cznr;
    }

    public void setCznr(String cznr) {
        this.updated_at = new Date();
        this.cznr = cznr;
    }

    public String getCzqd() {
        return czqd;
    }

    public void setCzqd(String czqd) {
        this.updated_at = new Date();
        this.czqd = czqd;
    }

    public Date getBlsj() {
        return blsj;
    }

    public void setBlsj(Date blsj) {
        this.updated_at = new Date();
        this.blsj = blsj;
    }

    public String getStep() {
        return step;
    }

    public void setStep(String step) {
        this.updated_at = new Date();
        this.step = step;
    }

    public String getYwlsh() {
        return ywlsh;
    }

    public void setYwlsh(String ywlsh) {
        this.updated_at = new Date();
        this.ywlsh = ywlsh;
    }

    public CFinanceDailyBusinessVice getcFinanceDailyBusinessVice() {
        return cFinanceDailyBusinessVice;
    }

    public void setcFinanceDailyBusinessVice(CFinanceDailyBusinessVice cFinanceDailyBusinessVice) {
        this.updated_at = new Date();
        this.cFinanceDailyBusinessVice = cFinanceDailyBusinessVice;
    }

    public String getZhclry() {
        return zhclry;
    }

    public void setZhclry(String zhclry) {
        this.updated_at = new Date();
        this.zhclry = zhclry;
    }

}
