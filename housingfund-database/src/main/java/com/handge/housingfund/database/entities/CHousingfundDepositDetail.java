package com.handge.housingfund.database.entities;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by tanyi on 2017/9/7.
 */
@Entity
@Table(name = "c_housingfund_deposit_detail")
@org.hibernate.annotations.Table(appliesTo = "c_housingfund_deposit_detail", comment = "住房公积金缴存使用情况表")
public class CHousingfundDepositDetail extends Common implements java.io.Serializable {

    private static final long serialVersionUID = -159472283135040394L;

    @Column(name = "XuHao", columnDefinition = "VARCHAR(30) DEFAULT NULL COMMENT '序号'")
    private String xuhao;
    @Column(name = "ZBMC", columnDefinition = "VARCHAR(30) DEFAULT NULL COMMENT '指标名称'")
    private String zbmc;
    @Column(name = "DaiMa", columnDefinition = "VARCHAR(6) DEFAULT NULL COMMENT '代码'")
    private String daima;
    @Column(name = "JLDW", columnDefinition = "VARCHAR(30) DEFAULT NULL COMMENT '计量单位'")
    private String jldw;
    /**
     * tanyi 20170927 修改 特殊字段，不用number型
     */
    @Column(name = "ShuLiang", columnDefinition = "VARCHER(255) DEFAULT NULL COMMENT '数量'")
    private String shuliang;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "cFinanceAccountPeriod", columnDefinition = "VARCHAR(32) DEFAULT NULL COMMENT '会计期间'")
    private CFinanceAccountPeriod cFinanceAccountPeriod;

    public CHousingfundDepositDetail() {
        super();
    }

    public CHousingfundDepositDetail(String id, Date created_at, Date updated_at, Date deleted_at, boolean deleted,
                                     String xuhao, String zbmc, String daima, String jldw, String shuliang,
                                     CFinanceAccountPeriod cFinanceAccountPeriod) {
        this.id = id;
        this.created_at = created_at;
        this.updated_at = updated_at;
        this.deleted_at = deleted_at;
        this.deleted = deleted;
        this.xuhao = xuhao;
        this.zbmc = zbmc;
        this.daima = daima;
        this.jldw = jldw;
        this.shuliang = shuliang;
        this.cFinanceAccountPeriod = cFinanceAccountPeriod;
    }

    public String getXuhao() {
        return xuhao;
    }

    public void setXuhao(String xuhao) {
        this.updated_at = new Date();
        this.xuhao = xuhao;
    }

    public String getZbmc() {
        return zbmc;
    }

    public void setZbmc(String zbmc) {
        this.updated_at = new Date();
        this.zbmc = zbmc;
    }

    public String getDaima() {
        return daima;
    }

    public void setDaima(String daima) {
        this.updated_at = new Date();
        this.daima = daima;
    }

    public String getJldw() {
        return jldw;
    }

    public void setJldw(String jldw) {
        this.updated_at = new Date();
        this.jldw = jldw;
    }

    public String getShuliang() {
        return shuliang;
    }

    public void setShuliang(String shuliang) {
        this.updated_at = new Date();
        this.shuliang = shuliang;
    }

    public CFinanceAccountPeriod getcFinanceAccountPeriod() {
        return cFinanceAccountPeriod;
    }

    public void setcFinanceAccountPeriod(CFinanceAccountPeriod cFinanceAccountPeriod) {
        this.updated_at = new Date();
        this.cFinanceAccountPeriod = cFinanceAccountPeriod;
    }

}
