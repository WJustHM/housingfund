package com.handge.housingfund.database.entities;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by tanyi on 2017/8/23.
 */
@Entity
@Table(name = "c_finance_daily_business_vice")
@org.hibernate.annotations.Table(appliesTo = "c_finance_daily_business_vice", comment = "日常业务处理 副表")
public class CFinanceDailyBusinessVice extends Common implements java.io.Serializable {

    private static final long serialVersionUID = -7572778530071601020L;
    @Column(name = "ZJYWLX", columnDefinition = "VARCHAR(255) DEFAULT NULL COMMENT '资金业务类型'")
    private String zjywlx;
    @Column(name = "YWMCID", nullable = false, columnDefinition = "VARCHAR(32) NOT NULL COMMENT '业务名称ID'")
    private String ywmcid;

    @Column(name = "MBBH", nullable = false, columnDefinition = "VARCHAR(10) NOT NULL COMMENT '凭证模板编号'")
    private String mbbh;

    @Column(name = "YWSJ", columnDefinition = "TEXT DEFAULT NULL COMMENT '业务数据json'")
    private String ywsj;

    @Column(name = "BLZL", columnDefinition = "TEXT DEFAULT NULL COMMENT '办理资料'")
    private String blzl;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "RCCWCL", columnDefinition = "VARCHAR(32) DEFAULT NULL COMMENT '日常业务处理'")
    private CFinanceBusinessProcess rccwcl;

    public CFinanceDailyBusinessVice() {
        super();


    }

    public CFinanceDailyBusinessVice(String id, Date created_at, Date updated_at, Date deleted_at, Boolean deleted,
                                     String zjywlx, String ywmcid, String ywsj, String blzl, CFinanceBusinessProcess rccwcl ,String mbbh) {
        this.id = id;
        this.created_at = created_at;
        this.updated_at = updated_at;
        this.deleted_at = deleted_at;
        this.deleted = deleted;
        this.zjywlx = zjywlx;
        this.ywsj = ywsj;
        this.blzl = blzl;
        this.rccwcl = rccwcl;
        this.ywmcid = ywmcid;
        this.mbbh = mbbh;
    }

    public String getZjywlx() {
        return zjywlx;
    }

    public void setZjywlx(String zjywlx) {
        this.updated_at = new Date();
        this.zjywlx = zjywlx;
    }

    public String getYwsj() {
        return ywsj;
    }

    public void setYwsj(String ywsj) {
        this.updated_at = new Date();
        this.ywsj = ywsj;
    }

    public String getBlzl() {
        return blzl;
    }

    public void setBlzl(String blzl) {
        this.updated_at = new Date();
        this.blzl = blzl;
    }

    public CFinanceBusinessProcess getRccwcl() {
        return rccwcl;
    }

    public void setRccwcl(CFinanceBusinessProcess rccwcl) {
        this.updated_at = new Date();
        this.rccwcl = rccwcl;
    }

    public String getYwmcid() {
        return ywmcid;
    }

    public void setYwmcid(String ywmcid) {
        this.updated_at = new Date();
        this.ywmcid = ywmcid;
    }

    public String getMbbh() {
        return mbbh;
    }

    public void setMbbh(String mbbh) {
        this.updated_at = new Date();
        this.mbbh = mbbh;
    }
}
