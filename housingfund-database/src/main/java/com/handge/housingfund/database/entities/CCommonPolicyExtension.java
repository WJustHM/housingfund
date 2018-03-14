package com.handge.housingfund.database.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by tanyi on 2017/9/19.
 */

@Entity
@Table(name = "c_common_policy_extension")
@org.hibernate.annotations.Table(appliesTo = "c_common_policy_extension", comment = "政策信息 扩展表")
public class CCommonPolicyExtension extends Common implements Serializable {

    private static final long serialVersionUID = -7038435338598228946L;

    @Column(name = "MingCheng", columnDefinition = "VARCHAR(20) DEFAULT NULL COMMENT '名称'")
    private String mingcheng;

    @Column(name = "SXRQ", columnDefinition = "DATETIME DEFAULT NULL COMMENT '生效日期'")
    private Date sxrq;

    @Column(name = "XGZ", columnDefinition = "NUMERIC(18,2) DEFAULT NULL COMMENT '修改值'")
    private BigDecimal xgz = BigDecimal.ZERO;

    @Column(name = "HKNLXS", columnDefinition = "NUMERIC(18,2) DEFAULT NULL COMMENT '还款能力系数'")
    private BigDecimal hknlxs = BigDecimal.ZERO;

    @Column(name = "DNGRZFGJJCKLL", columnDefinition = "NUMERIC(8,7) DEFAULT NULL COMMENT '当年个人住房公积金存款利率'")
    private BigDecimal dngrzfgjjckll = BigDecimal.ZERO;

    @Column(name = "SNGRZFGJJCKLL", columnDefinition = "NUMERIC(8,7) DEFAULT NULL COMMENT '上年个人住房公积金存款利率'")
    private BigDecimal sngrzfgjjckll = BigDecimal.ZERO;


    public CCommonPolicyExtension(){}

    public CCommonPolicyExtension(String id, Date created_at, Date updated_at, Date deleted_at, boolean deleted, String mingcheng, Date sxrq, BigDecimal xgz) {
        this.id = id;
        this.created_at = created_at;
        this.updated_at = updated_at;
        this.deleted_at = deleted_at;
        this.deleted = deleted;
        this.mingcheng = mingcheng;
        this.sxrq = sxrq;
        this.xgz = xgz;
    }

    public String getMingcheng() {
        return mingcheng;
    }

    public void setMingcheng(String mingcheng) {
        this.mingcheng = mingcheng;
    }

    public Date getSxrq() {
        return sxrq;
    }

    public void setSxrq(Date sxrq) {
        this.sxrq = sxrq;
    }

    public BigDecimal getXgz() {
        return xgz;
    }

    public void setXgz(BigDecimal xgz) {
        this.xgz = xgz;
    }

    public BigDecimal getHknlxs() {
        return hknlxs;
    }

    public void setHknlxs(BigDecimal hknlxs) {
        this.hknlxs = hknlxs;
    }

    public BigDecimal getDngrzfgjjckll() {
        return dngrzfgjjckll;
    }

    public void setDngrzfgjjckll(BigDecimal dngrzfgjjckll) {
        this.dngrzfgjjckll = dngrzfgjjckll;
    }

    public BigDecimal getSngrzfgjjckll() {
        return sngrzfgjjckll;
    }

    public void setSngrzfgjjckll(BigDecimal sngrzfgjjckll) {
        this.sngrzfgjjckll = sngrzfgjjckll;
    }
}
