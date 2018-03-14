package com.handge.housingfund.database.entities;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "c_collection_withdrawl_business_extension")
@org.hibernate.annotations.Table(appliesTo = "c_collection_withdrawl_business_extension", comment = "提取业务扩展表")
public class CCollectionWithdrawlBusinessExtension extends Common implements java.io.Serializable {

    @Column(name = "QTTQBZ", columnDefinition = "VARCHAR(255) DEFAULT NULL COMMENT '其他提取备注'")
    private String qttqbz;
    @Column(name = "SKYHZHHM", columnDefinition = "VARCHAR(120) DEFAULT NULL COMMENT '收款银行账户号码'")
    private String skyhzhhm;
    @Column(name = "SKYHHM", columnDefinition = "VARCHAR(120) DEFAULT NULL COMMENT '收款银行户名'")
    private String skyhhm;
    @Column(name = "SKYHMC", columnDefinition = "VARCHAR(120) DEFAULT NULL COMMENT '收款银行名称'")
    private String skyhmc;
    @Column(name = "ZongE", columnDefinition = "NUMERIC(18,2) DEFAULT NULL COMMENT '总额'")
    private BigDecimal zongE = BigDecimal.ZERO;
    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "details", columnDefinition = "VARCHAR(32) DEFAULT NULL COMMENT '提取业务扩展'")
    private StCollectionPersonalBusinessDetails stCollectionPersonalBusinessDetails;

    public BigDecimal getZongE() {
        return zongE;
    }

    public void setZongE(BigDecimal zongE) {
        this.zongE = zongE;
    }

    public String getQttqbz() {
        return qttqbz;
    }

    public void setQttqbz(String qttqbz) {
        this.qttqbz = qttqbz;
    }

    public String getSkyhzhhm() {
        return skyhzhhm;
    }

    public void setSkyhzhhm(String skyhzhhm) {
        this.skyhzhhm = skyhzhhm;
    }

    public String getSkyhhm() {
        return skyhhm;
    }

    public void setSkyhhm(String skyhhm) {
        this.skyhhm = skyhhm;
    }

    public String getSkyhmc() {
        return skyhmc;
    }

    public void setSkyhmc(String skyhmc) {
        this.skyhmc = skyhmc;
    }

    public StCollectionPersonalBusinessDetails getStCollectionPersonalBusinessDetails() {
        return stCollectionPersonalBusinessDetails;
    }

    public void setStCollectionPersonalBusinessDetails(StCollectionPersonalBusinessDetails stCollectionPersonalBusinessDetails) {
        this.stCollectionPersonalBusinessDetails = stCollectionPersonalBusinessDetails;
    }
}
