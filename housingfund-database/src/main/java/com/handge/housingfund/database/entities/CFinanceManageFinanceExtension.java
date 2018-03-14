package com.handge.housingfund.database.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

/**
 * Created by Administrator on 2017/9/13.
 */
@Entity
@Table(name = "c_finance_manage_finance_extension")
@org.hibernate.annotations.Table(appliesTo = "c_finance_manage_finance_extension", comment = "理财业务扩展表")
public class CFinanceManageFinanceExtension extends Common implements Serializable{

    private static final long serialVersionUID = 2198181657572909755L;

    @Column(name = "DDSJ", columnDefinition = "DATETIME DEFAULT NULL COMMENT '到达时间'")
    private Date ddsj;
    @Column(name = "SHSJ", columnDefinition = "DATETIME DEFAULT NULL COMMENT '审核时间（即审核列表中的受理时间）'")
    private Date shsj;
    @Column(name = "BJSJ", columnDefinition = "DATETIME DEFAULT NULL COMMENT '办结时间'")
    private Date bjsj;
    @Column(name = "BEIZHU", columnDefinition = "TEXT DEFAULT NULL COMMENT '备注'")
    private String beizhu;
    @Column(name = "SBYY", columnDefinition = "TEXT DEFAULT NULL COMMENT '失败原因'")
    private String sbyy;
    @Column(name = "RGCL", columnDefinition = "VARCHAR(1) DEFAULT '0' COMMENT '是否人工处理 0:否 1:是'")
    private String rgcl;
    @Column(name = "STEP", columnDefinition = "VARCHAR(20) DEFAULT NULL COMMENT '状态机状态'")
    private String step;
    @Column(name = "CZMC", columnDefinition = "VARCHAR(20) DEFAULT NULL COMMENT '操作名称'")
    private String czmc;
    @Column(name = "CZY", columnDefinition = "VARCHAR(120) DEFAULT NULL COMMENT '操作员'")
    private String czy;
    @Column(name = "YWLX", columnDefinition = "VARCHAR(6) DEFAULT NULL COMMENT '业务类型 09：定期存款 10：定期支取 11：通知存款 12：通知存款支取'")
    private String ywlx;
    @Column(name = "SHYBH", columnDefinition = "TEXT DEFAULT NULL COMMENT '审核员编号'")
    private String shybh;

    public String getShybh() {
        return shybh;
    }

    public void setShybh(String shybh) {
        this.shybh = shybh;
    }

    public Date getDdsj() {
        return ddsj;
    }

    public void setDdsj(Date ddsj) {
        this.ddsj = ddsj;
    }

    public Date getShsj() {
        return shsj;
    }

    public void setShsj(Date shsj) {
        this.shsj = shsj;
    }

    public Date getBjsj() {
        return bjsj;
    }

    public void setBjsj(Date bjsj) {
        this.bjsj = bjsj;
    }

    public String getBeizhu() {
        return beizhu;
    }

    public void setBeizhu(String beizhu) {
        this.beizhu = beizhu;
    }

    public String getSbyy() {
        return sbyy;
    }

    public void setSbyy(String sbyy) {
        this.sbyy = sbyy;
    }

    public String getRgcl() {
        return rgcl;
    }

    public void setRgcl(String rgcl) {
        this.rgcl = rgcl;
    }

    public String getStep() {
        return step;
    }

    public void setStep(String step) {
        this.step = step;
    }

    public String getCzmc() {
        return czmc;
    }

    public void setCzmc(String czmc) {
        this.czmc = czmc;
    }

    public String getCzy() {
        return czy;
    }

    public void setCzy(String czy) {
        this.czy = czy;
    }

    public String getYwlx() {
        return ywlx;
    }

    public void setYwlx(String ywlx) {
        this.ywlx = ywlx;
    }

    public CFinanceManageFinanceExtension() {
    }

    public CFinanceManageFinanceExtension(String id, Date created_at, Date updated_at, Date deleted_at, boolean deleted, Date ddsj, Date shsj, Date bjsj, String beizhu, String sbyy, String rgcl, String step, String czmc, String czy, String ywlx, String shybh) {
        this.id = id;
        this.created_at = created_at;
        this.updated_at = updated_at;
        this.deleted_at = deleted_at;
        this.deleted = deleted;
        this.ddsj = ddsj;
        this.shsj = shsj;
        this.bjsj = bjsj;
        this.beizhu = beizhu;
        this.sbyy = sbyy;
        this.rgcl = rgcl;
        this.step = step;
        this.czmc = czmc;
        this.czy = czy;
        this.ywlx = ywlx;
        this.shybh = shybh;
    }
}
