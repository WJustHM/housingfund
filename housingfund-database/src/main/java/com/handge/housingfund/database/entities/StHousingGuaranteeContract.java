package com.handge.housingfund.database.entities;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "st_housing_guarantee_contract",indexes = {
        @Index(name = "INDEX_id", columnList = "id", unique = true)})
@org.hibernate.annotations.Table(appliesTo = "st_housing_guarantee_contract", comment = "担保合同信息 表6.0.3")
public class StHousingGuaranteeContract extends Common implements java.io.Serializable {

    private static final long serialVersionUID = 2277572000251847367L;
    @Column(name = "DBHTBH", columnDefinition = "VARCHAR(30) DEFAULT NULL COMMENT '担保合同编号'")
    private String dbhtbh;
    @Column(name = "JKHTBH", columnDefinition = "VARCHAR(30) DEFAULT NULL COMMENT '借款合同编号'")
    private String jkhtbh;
    @Column(name = "DKDBLX", columnDefinition = "VARCHAR(2) DEFAULT NULL COMMENT '贷款担保类型'")
    private String dkdblx;
    @Column(name = "DBJGMC", columnDefinition = "VARCHAR(255) DEFAULT NULL COMMENT '担保机构名称'")
    private String dbjgmc;
    @Column(name = "DYWQZH", columnDefinition = "VARCHAR(30) DEFAULT NULL COMMENT '抵押物权证号'")
    private String dywqzh;
    @Column(name = "DYWTXQZH", columnDefinition = "VARCHAR(30) DEFAULT NULL COMMENT '抵押物他项权证号'")
    private String dywtxqzh;
    @Column(name = "DYWFWZL", columnDefinition = "VARCHAR(255) DEFAULT NULL COMMENT '抵押物房屋坐落'")
    private String dywfwzl;
    @Column(name = "DYQJLRQ", columnDefinition = "DATETIME DEFAULT NULL COMMENT '抵押权建立日期'")
    private Date dyqjlrq;
    @Column(name = "DYQJCRQ", columnDefinition = "DATETIME DEFAULT NULL COMMENT '抵押权解除日期'")
    private Date dyqjcrq;
    @Column(name = "DYWPGJZ", columnDefinition = "NUMERIC(18,2) DEFAULT NULL COMMENT '抵押物评估价值'")
    private BigDecimal dywpgjz = BigDecimal.ZERO;
    @Column(name = "BZHTBH", columnDefinition = "VARCHAR(30) DEFAULT NULL COMMENT '保证合同编号'")
    private String bzhtbh;
    @Column(name = "BZJGMC", columnDefinition = "VARCHAR(255) DEFAULT NULL COMMENT '保证机构名称'")
    private String bzjgmc;
    @Column(name = "DKBZJ", columnDefinition = "NUMERIC(18,2) DEFAULT NULL COMMENT '贷款保证金'")
    private BigDecimal dkbzj = BigDecimal.ZERO;
    @Column(name = "FHBZJRQ", columnDefinition = "DATETIME DEFAULT NULL COMMENT '返还保证金日期'")
    private Date fhbzjrq;
    @Column(name = "ZYHTBH", columnDefinition = "VARCHAR(30) DEFAULT NULL COMMENT '质押合同编号'")
    private String zyhtbh;
    @Column(name = "ZYWBH", columnDefinition = "VARCHAR(30) DEFAULT NULL COMMENT '质押物编号'")
    private String zywbh;
    @Column(name = "ZYWMC", columnDefinition = "VARCHAR(255) DEFAULT NULL COMMENT '质押物名称'")
    private String zywmc;
    @Column(name = "ZYWJZ", columnDefinition = "NUMERIC(18,2) DEFAULT NULL COMMENT '质押物价值'")
    private BigDecimal zywjz = BigDecimal.ZERO;
    @Column(name = "ZYHTKSRQ", columnDefinition = "DATETIME DEFAULT NULL COMMENT '质押合同开始日期'")
    private Date zyhtksrq;
    @Column(name = "ZYHTJSRQ", columnDefinition = "DATETIME DEFAULT NULL COMMENT '质押合同结束日期'")
    private Date zyhtjsrq;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "guarantee_extenstions", columnDefinition = "VARCHAR(32) DEFAULT NULL COMMENT '保证表'")
    private List<CLoanGuaranteeExtension> cLoanGuaranteeExtensions;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "guarantee_pledge_extenstions", columnDefinition = "VARCHAR(32) DEFAULT NULL COMMENT '质押表'")
    private List<CLoanGuaranteePledgeExtension> cLoanGuaranteePledgeExtensions;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "guarantee_mortgage_extenstions", columnDefinition = "VARCHAR(32) DEFAULT NULL COMMENT '抵押表'")
    private List<CLoanGuaranteeMortgageExtension> cLoanGuaranteeMortgageExtensions;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "extension", columnDefinition = "VARCHAR(32) DEFAULT NULL COMMENT '担保合同基础拓展表'")
    private CHousingGuaranteeContractExtension extension;

    public StHousingGuaranteeContract(){
       super();


    }

    public StHousingGuaranteeContract(String id, Date created_at, Date updated_at, Date deleted_at, boolean deleted,
                                      String dbhtbh, String jkhtbh, String dkdblx, String dbjgmc, String dywqzh, String dywtxqzh,
                                      Date dyqjlrq, Date dyqjcrq, BigDecimal dywpgjz, String bzhtbh, String bzjgmc, BigDecimal dkbzj, String dywfwzl,
                                      Date fhbzjrq, String zyhtbh, String zywbh, String zywmc, BigDecimal zywjz, Date zyhtksrq, Date zyhtjsrq, List<CLoanGuaranteeExtension> cLoanGuaranteeExtensions,
                                      List<CLoanGuaranteePledgeExtension> cLoanGuaranteePledgeExtensions, List<CLoanGuaranteeMortgageExtension> cLoanGuaranteeMortgageExtensions,
                                      CHousingGuaranteeContractExtension extension) {
        this.id = id;
        this.created_at = created_at;
        this.updated_at = updated_at;
        this.deleted_at = deleted_at;
        this.deleted = deleted;
        this.dbhtbh = dbhtbh;
        this.jkhtbh = jkhtbh;
        this.dkdblx = dkdblx;
        this.dbjgmc = dbjgmc;
        this.dywqzh = dywqzh;
        this.dywtxqzh = dywtxqzh;
        this.dyqjlrq = dyqjlrq;
        this.dyqjcrq = dyqjcrq;
        this.dywpgjz = dywpgjz;
        this.bzhtbh = bzhtbh;
        this.bzjgmc = bzjgmc;
        this.dkbzj = dkbzj;
        this.dywfwzl = dywfwzl;
        this.fhbzjrq = fhbzjrq;
        this.zyhtbh = zyhtbh;
        this.zywbh = zywbh;
        this.zywmc = zywmc;
        this.zywjz = zywjz;
        this.zyhtksrq = zyhtksrq;
        this.zyhtjsrq = zyhtjsrq;
        this.cLoanGuaranteeExtensions = cLoanGuaranteeExtensions;
        this.cLoanGuaranteeMortgageExtensions = cLoanGuaranteeMortgageExtensions;
        this.cLoanGuaranteePledgeExtensions = cLoanGuaranteePledgeExtensions;
        this.extension = extension;
    }

    public List<CLoanGuaranteeExtension> getcLoanGuaranteeExtensions() {
        return cLoanGuaranteeExtensions;
    }

    public void setcLoanGuaranteeExtensions(List<CLoanGuaranteeExtension> cLoanGuaranteeExtensions) {
       this.updated_at = new Date();
        this.cLoanGuaranteeExtensions = cLoanGuaranteeExtensions;
    }

    public List<CLoanGuaranteePledgeExtension> getcLoanGuaranteePledgeExtensions() {
        return cLoanGuaranteePledgeExtensions;
    }

    public void setcLoanGuaranteePledgeExtensions(List<CLoanGuaranteePledgeExtension> cLoanGuaranteePledgeExtensions) {
       this.updated_at = new Date();
        this.cLoanGuaranteePledgeExtensions = cLoanGuaranteePledgeExtensions;
    }

    public List<CLoanGuaranteeMortgageExtension> getcLoanGuaranteeMortgageExtensions() {
        return cLoanGuaranteeMortgageExtensions;
    }

    public void setcLoanGuaranteeMortgageExtensions(List<CLoanGuaranteeMortgageExtension> cLoanGuaranteeMortgageExtensions) {
       this.updated_at = new Date();
        this.cLoanGuaranteeMortgageExtensions = cLoanGuaranteeMortgageExtensions;
    }

    public String getDbhtbh() {
        return this.dbhtbh;
    }

    public void setDbhtbh(String dbhtbh) {
       this.updated_at = new Date();
        this.dbhtbh = dbhtbh;
    }

    public String getJkhtbh() {
        return this.jkhtbh;
    }

    public void setJkhtbh(String jkhtbh) {
       this.updated_at = new Date();
        this.jkhtbh = jkhtbh;
    }

    public String getDkdblx() {
        return this.dkdblx;
    }

    public void setDkdblx(String dkdblx) {
       this.updated_at = new Date();
        this.dkdblx = dkdblx;
    }

    public String getDbjgmc() {
        return this.dbjgmc;
    }

    public void setDbjgmc(String dbjgmc) {
       this.updated_at = new Date();
        this.dbjgmc = dbjgmc;
    }

    public String getDywqzh() {
        return this.dywqzh;
    }

    public void setDywqzh(String dywqzh) {
       this.updated_at = new Date();
        this.dywqzh = dywqzh;
    }

    public String getDywtxqzh() {
        return this.dywtxqzh;
    }

    public void setDywtxqzh(String dywtxqzh) {
       this.updated_at = new Date();
        this.dywtxqzh = dywtxqzh;
    }

    public Date getDyqjlrq() {
        return this.dyqjlrq;
    }

    public void setDyqjlrq(Date dyqjlrq) {
       this.updated_at = new Date();
        this.dyqjlrq = dyqjlrq;
    }

    public Date getDyqjcrq() {
        return this.dyqjcrq;
    }

    public void setDyqjcrq(Date dyqjcrq) {
       this.updated_at = new Date();
        this.dyqjcrq = dyqjcrq;
    }

    public BigDecimal getDywpgjz() {
        return this.dywpgjz;
    }

    public void setDywpgjz(BigDecimal dywpgjz) {
       this.updated_at = new Date();
        this.dywpgjz = dywpgjz;
    }

    public String getBzhtbh() {
        return this.bzhtbh;
    }

    public void setBzhtbh(String bzhtbh) {
       this.updated_at = new Date();
        this.bzhtbh = bzhtbh;
    }

    public String getBzjgmc() {
        return this.bzjgmc;
    }

    public void setBzjgmc(String bzjgmc) {
       this.updated_at = new Date();
        this.bzjgmc = bzjgmc;
    }

    public BigDecimal getDkbzj() {
        return this.dkbzj;
    }

    public void setDkbzj(BigDecimal dkbzj) {
       this.updated_at = new Date();
        this.dkbzj = dkbzj;
    }

    public Date getFhbzjrq() {
        return this.fhbzjrq;
    }

    public void setFhbzjrq(Date fhbzjrq) {
       this.updated_at = new Date();
        this.fhbzjrq = fhbzjrq;
    }

    public String getZyhtbh() {
        return this.zyhtbh;
    }

    public void setZyhtbh(String zyhtbh) {
       this.updated_at = new Date();
        this.zyhtbh = zyhtbh;
    }

    public String getZywbh() {
        return this.zywbh;
    }

    public void setZywbh(String zywbh) {
       this.updated_at = new Date();
        this.zywbh = zywbh;
    }

    public String getZywmc() {
        return this.zywmc;
    }

    public void setZywmc(String zywmc) {
       this.updated_at = new Date();
        this.zywmc = zywmc;
    }

    public BigDecimal getZywjz() {
        return this.zywjz;
    }

    public void setZywjz(BigDecimal zywjz) {
       this.updated_at = new Date();
        this.zywjz = zywjz;
    }

    public Date getZyhtksrq() {
        return this.zyhtksrq;
    }

    public void setZyhtksrq(Date zyhtksrq) {
       this.updated_at = new Date();
        this.zyhtksrq = zyhtksrq;
    }

    public Date getZyhtjsrq() {
        return this.zyhtjsrq;
    }

    public void setZyhtjsrq(Date zyhtjsrq) {
       this.updated_at = new Date();
        this.zyhtjsrq = zyhtjsrq;
    }

    public String getDywfwzl() {
        return dywfwzl;
    }

    public void setDywfwzl(String dywfwzl) {
       this.updated_at = new Date();
        this.dywfwzl = dywfwzl;
    }

    public CHousingGuaranteeContractExtension getExtension() {
        return extension;
    }

    public void setExtension(CHousingGuaranteeContractExtension extension) {
       this.updated_at = new Date();
        this.extension = extension;
    }
}
