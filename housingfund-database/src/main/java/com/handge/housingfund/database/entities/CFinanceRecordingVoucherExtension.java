package com.handge.housingfund.database.entities;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * Created by tanyi on 2017/8/23.
 */
@Entity
@Table(name = "c_finance_recording_voucher_extension")
@org.hibernate.annotations.Table(appliesTo = "c_finance_recording_voucher_extension", comment = "记账凭证信息 扩展表")
public class CFinanceRecordingVoucherExtension extends Common implements Serializable {

    private static final long serialVersionUID = -1635179074400148251L;

    @Column(name = "HSDW", columnDefinition = "VARCHAR(255) DEFAULT NULL COMMENT '核算单位'")
    private String hsdw;

    @Column(name = "JFHJ", columnDefinition = "NUMERIC(18,2) DEFAULT NULL COMMENT '借方合计'")
    private BigDecimal jfhj = BigDecimal.ZERO;

    @Column(name = "DFHJ", columnDefinition = "NUMERIC(18,2) DEFAULT NULL COMMENT '贷方合计'")
    private BigDecimal dfhj = BigDecimal.ZERO;

    @Column(name = "CWZG", columnDefinition = "VARCHAR(120) DEFAULT NULL COMMENT '财务主管'")
    private String cwzg;

    @Column(name = "JiZhang", columnDefinition = "VARCHAR(120) DEFAULT NULL COMMENT '记账'")
    private String jizhang;

    @Column(name = "FuHe", columnDefinition = "VARCHAR(120) DEFAULT NULL COMMENT '复核'")
    private String fuhe;

    @Column(name = "ChuNa", columnDefinition = "VARCHAR(120) DEFAULT NULL COMMENT '出纳'")
    private String chuna;

    @Column(name = "ZhiDan", columnDefinition = "VARCHAR(120) DEFAULT NULL COMMENT '制单'")
    private String zhidan;

    @Column(name = "YWMC", columnDefinition = "VARCHAR(20) DEFAULT NULL COMMENT '业务名称'")
    private String ywmc;

    @Column(name = "YWLX", columnDefinition = "VARCHAR(20) DEFAULT NULL COMMENT '资金业务类型'")
    private String ywlx;

    @Column(name = "YWLSH", columnDefinition = "VARCHAR(20) DEFAULT NULL COMMENT '业务流水号'")
    private String ywlsh;

    @Column(name = "CZNR", columnDefinition = "VARCHAR(20) DEFAULT NULL COMMENT '操作内容'")
    private String cznr;

    @Column(name = "SFZJL", columnDefinition = "BIT(1) DEFAULT 0 COMMENT '是否主记录'")
    private Boolean sfzjl;

    @Column(name = "KMYEFX", columnDefinition = "VARCHAR(2) DEFAULT NULL COMMENT '科目余额方向 附A.27'")
    private String kmyefx;

    @Column(name = "SFJZ", columnDefinition = "BIT(1) DEFAULT 0 COMMENT '是否结账'")
    private Boolean sfjz;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "cFinanceRecordingVoucherExtension")
    private List<CBankAccChangeNotice> cBankAccChangeNotices;


    public CFinanceRecordingVoucherExtension() {
        super();

    }

    public CFinanceRecordingVoucherExtension(String id, Date created_at, Date updated_at, Date deleted_at, boolean deleted, String hsdw, BigDecimal jfhj, BigDecimal dfhj, String cwzg,
                                             String jizhang, String fuhe, String chuna, String zhidan, String ywmc, String ywlx,
                                             List<CBankAccChangeNotice> cBankAccChangeNotices, String ywlsh, String cznr, Boolean sfzjl, String kmyefx,
                                             Boolean sfjz) {
        this.id = id;
        this.created_at = created_at;
        this.updated_at = updated_at;
        this.deleted_at = deleted_at;
        this.deleted = deleted;
        this.hsdw = hsdw;
        this.jfhj = jfhj;
        this.dfhj = dfhj;
        this.cwzg = cwzg;
        this.jizhang = jizhang;
        this.fuhe = fuhe;
        this.chuna = chuna;
        this.zhidan = zhidan;
        this.ywmc = ywmc;
        this.ywlx = ywlx;
        this.cBankAccChangeNotices = cBankAccChangeNotices;
        this.ywlsh = ywlsh;
        this.cznr = cznr;
        this.sfzjl = sfzjl;
        this.kmyefx = kmyefx;
        this.sfjz = sfjz;
    }

    public Boolean getSfjz() {
        return sfjz;
    }

    public void setSfjz(Boolean sfjz) {
        this.updated_at = new Date();
        this.sfjz = sfjz;
    }

    public String getKmyefx() {
        return kmyefx;
    }

    public void setKmyefx(String kmyefx) {
        this.updated_at = new Date();
        this.kmyefx = kmyefx;
    }

    public String getCznr() {
        return cznr;
    }

    public void setCznr(String cznr) {
        this.updated_at = new Date();
        this.cznr = cznr;
    }

    public String getYwmc() {
        return ywmc;
    }

    public void setYwmc(String ywmc) {
        this.updated_at = new Date();
        this.ywmc = ywmc;
    }

    public String getYwlx() {
        return ywlx;
    }

    public void setYwlx(String ywlx) {
        this.updated_at = new Date();
        this.ywlx = ywlx;
    }

    public BigDecimal getJfhj() {
        return jfhj;
    }

    public void setJfhj(BigDecimal jfhj) {
        this.updated_at = new Date();
        this.jfhj = jfhj;
    }

    public BigDecimal getDfhj() {
        return dfhj;
    }

    public void setDfhj(BigDecimal dfhj) {
        this.updated_at = new Date();
        this.dfhj = dfhj;
    }

    public Boolean getSfzjl() {
        return sfzjl;
    }

    public void setSfzjl(Boolean sfzjl) {
        this.updated_at = new Date();
        this.sfzjl = sfzjl;
    }

    public String getHsdw() {
        return hsdw;
    }

    public void setHsdw(String hsdw) {
        this.updated_at = new Date();
        this.hsdw = hsdw;
    }

    public String getCwzg() {
        return cwzg;
    }

    public void setCwzg(String cwzg) {
        this.updated_at = new Date();
        this.cwzg = cwzg;
    }

    public String getJizhang() {
        return jizhang;
    }

    public void setJizhang(String jizhang) {
        this.updated_at = new Date();
        this.jizhang = jizhang;
    }

    public String getFuhe() {
        return fuhe;
    }

    public void setFuhe(String fuhe) {
        this.updated_at = new Date();
        this.fuhe = fuhe;
    }

    public String getChuna() {
        return chuna;
    }

    public void setChuna(String chuna) {
        this.updated_at = new Date();
        this.chuna = chuna;
    }

    public String getZhidan() {
        return zhidan;
    }

    public void setZhidan(String zhidan) {
        this.updated_at = new Date();
        this.zhidan = zhidan;
    }

    public List<CBankAccChangeNotice> getcBankAccChangeNotices() {
        return cBankAccChangeNotices;
    }

    public void setcBankAccChangeNotices(List<CBankAccChangeNotice> cBankAccChangeNotices) {
        if (cBankAccChangeNotices != null) {
            for (CBankAccChangeNotice cBankAccChangeNotice : cBankAccChangeNotices) {
                cBankAccChangeNotice.setcFinanceRecordingVoucherExtension(this);
            }
        }
        this.updated_at = new Date();
        this.cBankAccChangeNotices = cBankAccChangeNotices;
    }

    public String getYwlsh() {
        return ywlsh;
    }

    public void setYwlsh(String ywlsh) {
        this.updated_at = new Date();
        this.ywlsh = ywlsh;
    }
}
