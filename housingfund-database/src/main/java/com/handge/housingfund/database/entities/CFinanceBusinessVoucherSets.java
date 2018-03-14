package com.handge.housingfund.database.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

/**
 * Created by tanyi on 2017/8/23.
 */
@Entity
@Table(name = "c_finance_business_voucher_sets", indexes = {
        @Index(name = "INDEX_YWMCID", columnList = "YWMCID", unique = true)})
@org.hibernate.annotations.Table(appliesTo = "c_finance_business_voucher_sets", comment = "业务凭证设置")
public class CFinanceBusinessVoucherSets extends Common implements Serializable {

    private static final long serialVersionUID = -8986191062641014510L;
    @Column(name = "YWMC", nullable = false, columnDefinition = "VARCHAR(20) NOT NULL COMMENT '业务名称'")
    private String ywmc;
    @Column(name = "YWMCID", nullable = false, columnDefinition = "VARCHAR(32) NOT NULL COMMENT '业务名称ID'")
    private String ywmcid;
    @Column(name = "MBBH", columnDefinition = "VARCHAR(20) DEFAULT NULL COMMENT '模板编号'")
    private String mbbh;
    // ([id:123,KMMC:name,KMBH:科目编号,KMYEFX:科目余额方向])
    @Column(name = "JFKM", nullable = false, columnDefinition = "TEXT NOT NULL COMMENT '借方科目'")
    private String jfkm;
    // ([id:123,KMMC:name,KMBH:科目编号,KMYEFX:科目余额方向])
    @Column(name = "DFKM", nullable = false, columnDefinition = "TEXT NOT NULL COMMENT '贷方科目'")
    private String dfkm;
    @Column(name = "BeiZhu", columnDefinition = "TEXT DEFAULT NULL COMMENT '备注'")
    private String beizhu;
    @Column(name = "CJR", columnDefinition = "VARCHAR(120) DEFAULT NULL COMMENT '创建人'")
    private String cjr;
    @Column(name = "CJSJ", columnDefinition = "DATETIME DEFAULT NULL COMMENT '创建时间'")
    private Date cjsj;
    @Column(name = "SFYSY", columnDefinition = "BIT(1) DEFAULT 0 COMMENT '是否已使用'")
    private Boolean sfysy;

    public CFinanceBusinessVoucherSets() {
        super();

    }

    public CFinanceBusinessVoucherSets(String id, Date created_at, Date updated_at, Date deleted_at, boolean deleted,
                                       String ywmc, String mbbh, String jfkm, String dfkm, String beizhu, String cjr, Date cjsj, boolean sfysy,
                                       String ywmcid) {
        this.id = id;
        this.created_at = created_at;
        this.updated_at = updated_at;
        this.deleted_at = deleted_at;
        this.deleted = deleted;
        this.ywmc = ywmc;
        this.mbbh = mbbh;
        this.jfkm = jfkm;
        this.dfkm = dfkm;
        this.beizhu = beizhu;
        this.cjr = cjr;
        this.cjsj = cjsj;
        this.sfysy = sfysy;
        this.ywmcid = ywmcid;
    }

    public String getYwmc() {
        return ywmc;
    }

    public void setYwmc(String ywmc) {
        this.updated_at = new Date();
        this.ywmc = ywmc;
    }

    public String getMbbh() {
        return mbbh;
    }

    public void setMbbh(String mbbh) {
        this.updated_at = new Date();
        this.mbbh = mbbh;
    }

    public String getJfkm() {
        return jfkm;
    }

    public void setJfkm(String jfkm) {
        this.updated_at = new Date();
        this.jfkm = jfkm;
    }

    public String getDfkm() {
        return dfkm;
    }

    public void setDfkm(String dfkm) {
        this.updated_at = new Date();
        this.dfkm = dfkm;
    }

    public String getBeizhu() {
        return beizhu;
    }

    public void setBeizhu(String beizhu) {
        this.updated_at = new Date();
        this.beizhu = beizhu;
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

    public boolean getSfysy() {
        return sfysy;
    }

    public void setSfysy(boolean sfysy) {
        this.updated_at = new Date();
        this.sfysy = sfysy;
    }

    public void setSfysy(Boolean sfysy) {
        this.updated_at = new Date();
        this.sfysy = sfysy;
    }

    public String getYwmcid() {
        return ywmcid;
    }

    public void setYwmcid(String ywmcid) {
        this.updated_at = new Date();
        this.ywmcid = ywmcid;
    }
}
