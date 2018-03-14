package com.handge.housingfund.database.entities;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

@Entity
@Table(name = "st_collection_personal_business_details", indexes = {
        @Index(name = "INDEX_GRYWLSH", columnList = "YWLSH", unique = true)})
@org.hibernate.annotations.Table(appliesTo = "st_collection_personal_business_details", comment = "个人业务明细信息 表5.0.5")
public class StCollectionPersonalBusinessDetails extends Common implements java.io.Serializable {

    /**
     *
     */
    private static final long serialVersionUID = -591053417116663294L;

    @Column(name = "GRZH", columnDefinition = "VARCHAR(20) DEFAULT NULL COMMENT '个人账号'")
    private String grzh;
    @Column(name = "JZRQ", columnDefinition = "DATETIME DEFAULT NULL COMMENT '记账日期'")
    private Date jzrq;
    @Column(name = "GJHTQYWLX", columnDefinition = "VARCHAR(2) DEFAULT NULL COMMENT '归集和提取业务类型'")
    private String gjhtqywlx;
    @Column(name = "FSE", columnDefinition = "NUMERIC(18,2) DEFAULT NULL COMMENT '发生额'")
    private BigDecimal fse = BigDecimal.ZERO;
    @Column(name = "DNGJFSE", columnDefinition = "NUMERIC(18,2) DEFAULT NULL COMMENT '当年归集发生额'")
    private BigDecimal dngjfse = BigDecimal.ZERO;
    @Column(name = "SNJZFSE", columnDefinition = "NUMERIC(18,2) DEFAULT NULL COMMENT '上年结转发生额'")
    private BigDecimal snjzfse = BigDecimal.ZERO;
    @Column(name = "FSLXE", columnDefinition = "NUMERIC(18,2) DEFAULT NULL COMMENT '发生利息额'")
    private BigDecimal fslxe = BigDecimal.ZERO;
    @Column(name = "TQYY", columnDefinition = "VARCHAR(2) DEFAULT NULL COMMENT '提取原因'")
    private String tqyy;
    @Column(name = "TQFS", columnDefinition = "VARCHAR(2) DEFAULT NULL COMMENT '提取方式'")
    private String tqfs;
    @Column(name = "YWLSH", columnDefinition = "VARCHAR(20) DEFAULT NULL COMMENT '业务流水号'")
    private String ywlsh;
    @Column(name = "CZBZ", columnDefinition = "VARCHAR(2) DEFAULT NULL COMMENT '冲账标识'")
    private String czbz;
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "extension", columnDefinition = "VARCHAR(32) DEFAULT NULL COMMENT '个人业务明细扩展'")
    private CCollectionPersonalBusinessDetailsExtension cCollectionPersonalBusinessDetailsExtension;
    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "Person", columnDefinition = "VARCHAR(32) DEFAULT NULL COMMENT '个人信息'")
    private StCommonPerson person;
    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "Unit", columnDefinition = "VARCHAR(32) DEFAULT NULL COMMENT '单位信息'")
    private StCommonUnit unit;
    @OneToOne(mappedBy = "grywmx", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private CCollectionIndividualAccountActionVice individualAccountActionVice;
    @OneToOne(mappedBy = "grywmx", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private CCollectionIndividualAccountBasicVice individualAccountBasicVice;
    @OneToOne(mappedBy = "grywmx", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private CCollectionIndividualAccountMergeVice individualAccountMergeVice;
    @OneToOne(mappedBy = "grywmx", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private CCollectionWithdrawlVice withdrawlVice;
    @OneToOne(mappedBy = "grywmx", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private CCollectionIndividualAccountTransferNewVice individualAccountTransferNewVice;
    /*@OneToOne(mappedBy = "grywmx", cascade = CascadeType.ALL, fetch = FetchType.LAZY)*/
    @Transient
    private CCollectionIndividualAccountTransferVice individualAccountTransferVice;
    @OneToOne(mappedBy = "grywmx", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private CCollectionAllochthounousTransferVice allochthounousTransferVice;

    @OneToOne(mappedBy = "stCollectionPersonalBusinessDetails",cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    private CCollectionWithdrawlBusinessExtension withdrawlBusinessExtension;

    public CCollectionWithdrawlBusinessExtension getWithdrawlBusinessExtension() {
        return withdrawlBusinessExtension;
    }

    public void setWithdrawlBusinessExtension(CCollectionWithdrawlBusinessExtension withdrawlBusinessExtension) {
        this.withdrawlBusinessExtension = withdrawlBusinessExtension;
    }

    public CCollectionAllochthounousTransferVice getAllochthounousTransferVice() {
        return allochthounousTransferVice;
    }

    public void setAllochthounousTransferVice(CCollectionAllochthounousTransferVice allochthounousTransferVice) {
        this.allochthounousTransferVice = allochthounousTransferVice;
    }

    public CCollectionIndividualAccountActionVice getIndividualAccountActionVice() {
        return individualAccountActionVice;
    }

    public void setIndividualAccountActionVice(CCollectionIndividualAccountActionVice individualAccountActionVice) {
       this.updated_at = new Date();
        this.individualAccountActionVice = individualAccountActionVice;
    }

    public CCollectionIndividualAccountBasicVice getIndividualAccountBasicVice() {
        return individualAccountBasicVice;
    }

    public void setIndividualAccountBasicVice(CCollectionIndividualAccountBasicVice individualAccountBasicVice) {
       this.updated_at = new Date();
        this.individualAccountBasicVice = individualAccountBasicVice;
    }

    public CCollectionIndividualAccountMergeVice getIndividualAccountMergeVice() {
        return individualAccountMergeVice;
    }

    public void setIndividualAccountMergeVice(CCollectionIndividualAccountMergeVice individualAccountMergeVice) {
       this.updated_at = new Date();
        this.individualAccountMergeVice = individualAccountMergeVice;
    }

    public CCollectionIndividualAccountTransferVice getIndividualAccountTransferVice() {
        return individualAccountTransferVice;
    }

    public void setIndividualAccountTransferVice(
            CCollectionIndividualAccountTransferVice individualAccountTransferVice) {
        this.individualAccountTransferVice = individualAccountTransferVice;
    }

    public CCollectionWithdrawlVice getWithdrawlVice() {
        return withdrawlVice;
    }

    public void setWithdrawlVice(CCollectionWithdrawlVice withdrawlVice) {
       this.updated_at = new Date();
        this.withdrawlVice = withdrawlVice;
    }

    public StCommonPerson getPerson() {
        return person;
    }

    public void setPerson(StCommonPerson person) {
       this.updated_at = new Date();
        this.person = person;
    }

    public StCommonUnit getUnit() {
        return unit;
    }

    public void setUnit(StCommonUnit unit) {
       this.updated_at = new Date();
        this.unit = unit;
    }

    public String getGrzh() {
        return this.grzh;
    }

    public void setGrzh(String grzh) {
       this.updated_at = new Date();
        this.grzh = grzh;
    }

    public Date getJzrq() {
        return this.jzrq;
    }

    public void setJzrq(Date jzrq) {
       this.updated_at = new Date();
        this.jzrq = jzrq;
    }

    public String getGjhtqywlx() {
        return this.gjhtqywlx;
    }

    public void setGjhtqywlx(String gjhtqywlx) {
       this.updated_at = new Date();
        this.gjhtqywlx = gjhtqywlx;
    }

    public BigDecimal getFse() {
        return this.fse;
    }

    public void setFse(BigDecimal fse) {
       this.updated_at = new Date();
        this.fse = fse;
    }

    public BigDecimal getDngjfse() {
        return this.dngjfse;
    }

    public void setDngjfse(BigDecimal dngjfse) {
       this.updated_at = new Date();
        this.dngjfse = dngjfse;
    }

    public BigDecimal getSnjzfse() {
        return this.snjzfse;
    }

    public void setSnjzfse(BigDecimal snjzfse) {
       this.updated_at = new Date();
        this.snjzfse = snjzfse;
    }

    public BigDecimal getFslxe() {
        return this.fslxe;
    }

    public void setFslxe(BigDecimal fslxe) {
       this.updated_at = new Date();
        this.fslxe = fslxe;
    }

    public String getTqyy() {
        return this.tqyy;
    }

    public void setTqyy(String tqyy) {
       this.updated_at = new Date();
        this.tqyy = tqyy;
    }

    public String getTqfs() {
        return this.tqfs;
    }

    public void setTqfs(String tqfs) {
       this.updated_at = new Date();
        this.tqfs = tqfs;
    }

    public String getYwlsh() {
        return this.ywlsh;
    }

    public void setYwlsh(String ywlsh) {
       this.updated_at = new Date();
        this.ywlsh = ywlsh;
    }

    public String getCzbz() {
        return this.czbz;
    }

    public void setCzbz(String czbz) {
       this.updated_at = new Date();
        this.czbz = czbz;
    }

    public CCollectionPersonalBusinessDetailsExtension getExtension() {

        return cCollectionPersonalBusinessDetailsExtension;

    }

    public void setExtension(CCollectionPersonalBusinessDetailsExtension extension) {
       this.updated_at = new Date();

        this.cCollectionPersonalBusinessDetailsExtension = extension;

    }

    public StCollectionPersonalBusinessDetails(){
       super();


    }

    public StCollectionPersonalBusinessDetails(String id, Date created_at, Date updated_at, Date deleted_at,
                                               boolean deleted, String grzh, Date jzrq, String gjhtqywlx, BigDecimal fse, BigDecimal dngjfse,
                                               BigDecimal snjzfse, BigDecimal fslxe, String tqyy, String tqfs, String ywlsh, String czbz,
                                               CCollectionPersonalBusinessDetailsExtension extension, StCommonPerson person, StCommonUnit unit,
                                               CCollectionIndividualAccountActionVice individualAccountActionVice,
                                               CCollectionIndividualAccountBasicVice individualAccountBasicVice,
                                               CCollectionIndividualAccountMergeVice individualAccountMergeVice,
                                               CCollectionIndividualAccountTransferVice individualAccountTransferVice,
                                               CCollectionAllochthounousTransferVice allochthounousTransferVice) {
        this.id = id;
        this.created_at = created_at;
        this.updated_at = updated_at;
        this.deleted_at = deleted_at;
        this.deleted = deleted;
        this.grzh = grzh;
        this.jzrq = jzrq;
        this.gjhtqywlx = gjhtqywlx;
        this.fse = fse;
        this.dngjfse = dngjfse;
        this.snjzfse = snjzfse;
        this.fslxe = fslxe;
        this.tqyy = tqyy;
        this.tqfs = tqfs;
        this.ywlsh = ywlsh;
        this.czbz = czbz;
        this.cCollectionPersonalBusinessDetailsExtension = extension;
        this.person = person;
        this.unit = unit;
        this.individualAccountActionVice = individualAccountActionVice;
        this.individualAccountBasicVice = individualAccountBasicVice;
        this.individualAccountMergeVice = individualAccountMergeVice;
        this.individualAccountTransferVice = individualAccountTransferVice;
        this.allochthounousTransferVice = allochthounousTransferVice;

    }

    public CCollectionIndividualAccountTransferNewVice getIndividualAccountTransferNewVice() {
        return individualAccountTransferNewVice;
    }

    public void setIndividualAccountTransferNewVice(CCollectionIndividualAccountTransferNewVice individualAccountTransferNewVice) {
        this.individualAccountTransferNewVice = individualAccountTransferNewVice;
    }
}