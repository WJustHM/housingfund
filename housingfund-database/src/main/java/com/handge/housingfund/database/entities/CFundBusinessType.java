package com.handge.housingfund.database.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

/**
 * Created by gxy on 17-10-12.
 */
@Entity
@Table(name = "c_fund_business_type")
@org.hibernate.annotations.Table(appliesTo = "c_fund_business_type", comment = "资金业务类型与业务类型对照表")
public class CFundBusinessType  extends Common implements Serializable {

    private static final long serialVersionUID = -2684980504445200697L;

    @Column(name = "ZJYWLXBM", columnDefinition = "VARCHAR(4) NOT NULL COMMENT '资金业务类型编码'")
    private String zjywlxbm;
    @Column(name = "ZJYWLX", columnDefinition = "VARCHAR(20) NOT NULL COMMENT '资金业务类型'")
    private String zjywlx;
    @Column(name = "YWMCID", nullable = false, columnDefinition = "VARCHAR(32) NOT NULL COMMENT '业务名称ID'")
    private String ywmcid;

    public CFundBusinessType() {
        super();
    }

    public CFundBusinessType(String zjywlxbm, String zjywlx, String ywmcid) {
        this.zjywlxbm = zjywlxbm;
        this.zjywlx = zjywlx;
        this.ywmcid = ywmcid;
    }

    public String getZjywlxbm() {
        return zjywlxbm;
    }

    public void setZjywlxbm(String zjywlxbm) {
        this.updated_at = new Date();
        this.zjywlxbm = zjywlxbm;
    }

    public String getZjywlx() {
        return zjywlx;
    }

    public void setZjywlx(String zjywlx) {
        this.updated_at = new Date();
        this.zjywlx = zjywlx;
    }

    public String getYwmcid() {
        return ywmcid;
    }

    public void setYwmcid(String ywmcid) {
        this.updated_at = new Date();
        this.ywmcid = ywmcid;
    }
}
