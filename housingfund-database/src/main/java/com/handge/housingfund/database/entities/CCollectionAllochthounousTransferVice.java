package com.handge.housingfund.database.entities;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * Created by Liujuhao on 2017/12/8.
 */
@Entity
@Table(name = "c_collection_allochthounous_transfer_vice")
@org.hibernate.annotations.Table(appliesTo = "c_collection_allochthounous_transfer_vice", comment = "个人转移接续业务副表")
public class CCollectionAllochthounousTransferVice extends Common implements Serializable {
    private static final long serialVersionUID = -4025147554493495978L;

    @Column(name = "ZCJGBH", columnDefinition = "VARCHAR(15) DEFAULT NULL COMMENT '转出机构编号'")
    String ZCJGBH;
    @Column(name = "ZCGJJZXMC", columnDefinition = "VARCHAR(240) DEFAULT NULL COMMENT '转出公积金中心名称'")
    String ZCGJJZXMC;
    @Column(name = "YGRZFGJJZH", columnDefinition = "VARCHAR(20) DEFAULT NULL COMMENT '原个人住房公积金账号'")
    String YGRZFGJJZH;
    @Column(name = "YGZDWMC", columnDefinition = "VARCHAR(255) DEFAULT NULL COMMENT '原工作单位名称'")
    String YGZDWMC;
    @Column(name = "ZJLX", columnDefinition = "VARCHAR(2) DEFAULT NULL COMMENT '证件类型'")
    String ZJLX;
    @Column(name = "ZJHM", columnDefinition = "VARCHAR(18) DEFAULT NULL COMMENT '证件号码'")
    String ZJHM;
    @Column(name = "ZGXM", columnDefinition = "VARCHAR(120) DEFAULT NULL COMMENT '职工姓名'")
    String ZGXM;
    @Column(name = "SJHM", columnDefinition = "VARCHAR(11) DEFAULT NULL COMMENT '手机号码'")
    String SJHM;
    @Column(name = "XZFGJJZH", columnDefinition = "VARCHAR(20) DEFAULT NULL COMMENT '现住房公积金账号'")
    String XZFGJJZH;
    @Column(name = "XGZDWMC", columnDefinition = "VARCHAR(255) DEFAULT NULL COMMENT '现工作单位名称'")
    String XGZDWMC;
    @Column(name = "ZRZJZHYHMC", columnDefinition = "VARCHAR(240) DEFAULT NULL COMMENT '转入公积金中心资金账户所属银行名称'")
    String ZRZJZHYHMC;
    @Column(name = "ZRZJZH", columnDefinition = "VARCHAR(32) DEFAULT NULL COMMENT '转入公积金中心资金账号'")
    String ZRZJZH;
    @Column(name = "ZRZJZHHM", columnDefinition = "VARCHAR(240) DEFAULT NULL COMMENT '转入公积金中心资金账号户名'")
    String ZRZJZHHM;
    @Column(name = "LXDHHCZ", columnDefinition = "VARCHAR(20) DEFAULT NULL COMMENT '联系电话或传真'")
    String LXDHHCZ;
    @Column(name = "BLZL", columnDefinition = "TEXT DEFAULT NULL COMMENT '办理资料'")
    String BLZL;
    @Column(name = "YZHBJJE", columnDefinition = "NUMERIC(18,2) DEFAULT NULL COMMENT '原账户本金金额'")
    BigDecimal YZHBJJE = BigDecimal.ZERO;
    @Column(name = "BNDLX", columnDefinition = "NUMERIC(18,2) DEFAULT NULL COMMENT '本年度利息'")
    BigDecimal BNDLX = BigDecimal.ZERO;
    @Column(name = "ZYJE", columnDefinition = "NUMERIC(18,2) DEFAULT NULL COMMENT '转移金额'")
    BigDecimal ZYJE = BigDecimal.ZERO;
    @Column(name = "FSE", columnDefinition = "NUMERIC(18,2) DEFAULT NULL COMMENT '发生额'")
    BigDecimal FSE = BigDecimal.ZERO;
    @Column(name = "FSLXE", columnDefinition = "NUMERIC(18,2) DEFAULT NULL COMMENT '发生利息额'")
    BigDecimal FSLXE = BigDecimal.ZERO;
    @Column(name = "KHRQ", columnDefinition = "DATETIME DEFAULT NULL COMMENT '开户日期'")
    Date KHRQ;
    @Column(name = "JZNY", columnDefinition = "VARCHAR(6) DEFAULT NULL COMMENT '缴至年月'")
    String JZNY;
    @Column(name = "LXJC", columnDefinition = "BIT(1) DEFAULT NULL COMMENT '连续缴存'")
    Boolean LXJC = false;
    @Column(name = "GJJDKCS", columnDefinition = "NUMERIC(10,0) DEFAULT NULL COMMENT '公积金贷款次数'")
    Integer GJJDKCS = 0;
    @Column(name = "JQDK", columnDefinition = "BIT(1) DEFAULT NULL COMMENT '结清贷款'")
    Boolean JQDK = false;
    @Column(name = "SFCZPTPD", columnDefinition = "BIT(1) DEFAULT NULL COMMENT '是否存在骗提骗贷'")
    Boolean SFCZPTPD = false;
    @Column(name = "YWLXDH", columnDefinition = "VARCHAR(20) DEFAULT NULL COMMENT '业务联系电话'")
    String YWLXDH;
    @Column(name = "LXDSCRQ", columnDefinition = "DATETIME DEFAULT NULL COMMENT '联系单生成日期'")
    Date LXDSCRQ;
    @Column(name = "FKYHMC", columnDefinition = "VARCHAR(255) DEFAULT NULL COMMENT '付款银行名称'")
    String FKYHMC;
    @Column(name = "FKZH", columnDefinition = "VARCHAR(32) DEFAULT NULL COMMENT '付款账号'")
    String FKZH;
    @Column(name = "FKHM", columnDefinition = "VARCHAR(32) DEFAULT NULL COMMENT '付款户名'")
    String FKHM;
    @Column(name = "ZRJGBH", columnDefinition = "VARCHAR(15) DEFAULT NULL COMMENT '转入机构编号'")
    String ZRJGBH;
    @Column(name = "ZRGJJZXMC", columnDefinition = "VARCHAR(240) DEFAULT NULL COMMENT '转入公积金中心名称'")
    String ZRGJJZXMC;
    @Column(name = "CZMC", columnDefinition = "VARCHAR(2) DEFAULT NULL COMMENT '操作名称（业务类型）'")
    String CZMC;
    @Column(name = "LXHBH", columnDefinition = "VARCHAR(16) DEFAULT NULL COMMENT '联系函编号'")
    String LXHBH;
    @Column(name = "LXHZT", columnDefinition = "VARCHAR(2) DEFAULT NULL COMMENT '联系函状态'")
    String LXHZT;
    @Column(name = "FKXX", columnDefinition = "VARCHAR(255) DEFAULT NULL COMMENT '反馈信息'")
    String FKXX;
    @Column(name = "PLLXHBH", columnDefinition = "VARCHAR(25) DEFAULT NULL COMMENT '批量联系函编号'")
    String PLLXHBH;
//    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
//    @JoinColumn(name = "processes", columnDefinition = "VARCHAR(32) DEFAULT NULL COMMENT '业务流程记录'")
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "transferVice")
    List<CCollectionAllochthounousTransferProcess> processes;
    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "GRYWMX", columnDefinition = "VARCHAR(32) DEFAULT NULL COMMENT '个人业务明细'")
    private StCollectionPersonalBusinessDetails grywmx;

    public CCollectionAllochthounousTransferVice() {
    }

    public CCollectionAllochthounousTransferVice(String id, Date created_at, Date updated_at, Date deleted_at, boolean deleted,
                                                 String ZCJGBH, String ZCGJJZXMC, String YGRZFGJJZH, String YGZDWMC, String ZJLX, String ZJHM,
                                                 String ZGXM, String SJHM, String XZFGJJZH, String XGZDWMC, String ZRZJZHYHMC, String ZRZJZH, String LXDHHCZ,
                                                 String BLZL, BigDecimal YZHBJJE, BigDecimal BNDLX, BigDecimal ZYJE, Date KHRQ, String JZNY, Boolean LXJC,
                                                 Integer GJJDKCS, Boolean JQDK, Boolean SFCZPTPD, String YWLXDH, Date LXDSCRQ, String FKYHMC, String FKHM,
                                                 String ZRJGBH, String ZRGJJZXMC, String CZMC, String LXHBH, String LXHZT, String FKZH, String ZRZJZHHM,
                                                 String FKXX, String PLLXHBH,BigDecimal FSE, BigDecimal FSLXE,
                                                 List<CCollectionAllochthounousTransferProcess> processes, StCollectionPersonalBusinessDetails grywmx) {
        this.id = id;
        this.created_at = created_at;
        this.updated_at = updated_at;
        this.deleted_at = deleted_at;
        this.deleted = deleted;
        this.ZCJGBH = ZCJGBH;
        this.ZCGJJZXMC = ZCGJJZXMC;
        this.YGRZFGJJZH = YGRZFGJJZH;
        this.YGZDWMC = YGZDWMC;
        this.ZJLX = ZJLX;
        this.ZJHM = ZJHM;
        this.ZGXM = ZGXM;
        this.SJHM = SJHM;
        this.XZFGJJZH = XZFGJJZH;
        this.XGZDWMC = XGZDWMC;
        this.ZRZJZHYHMC = ZRZJZHYHMC;
        this.ZRZJZH = ZRZJZH;
        this.LXDHHCZ = LXDHHCZ;
        this.BLZL = BLZL;
        this.YZHBJJE = YZHBJJE;
        this.BNDLX = BNDLX;
        this.ZYJE = ZYJE;
        this.KHRQ = KHRQ;
        this.JZNY = JZNY;
        this.LXJC = LXJC;
        this.GJJDKCS = GJJDKCS;
        this.JQDK = JQDK;
        this.SFCZPTPD = SFCZPTPD;
        this.YWLXDH = YWLXDH;
        this.LXDSCRQ = LXDSCRQ;
        this.FKYHMC = FKYHMC;
        this.FKHM = FKHM;
        this.ZRJGBH = ZRJGBH;
        this.ZRGJJZXMC = ZRGJJZXMC;
        this.CZMC = CZMC;
        this.LXHBH = LXHBH;
        this.LXHZT = LXHZT;
        this.FKZH = FKZH;
        this.ZRZJZHHM = ZRZJZHHM;
        this.FKXX = FKXX;
        this.PLLXHBH = PLLXHBH;
        this.FSE = FSE;
        this.FSLXE = FSLXE;
        this.processes = processes;
        this.grywmx = grywmx;
    }

    public BigDecimal getFSE() {
        return FSE;
    }

    public void setFSE(BigDecimal FSE) {
        this.FSE = FSE;
    }

    public BigDecimal getFSLXE() {
        return FSLXE;
    }

    public void setFSLXE(BigDecimal FSLXE) {
        this.FSLXE = FSLXE;
    }

    public String getPLLXHBH() {
        return PLLXHBH;
    }

    public void setPLLXHBH(String PLLXHBH) {
        this.PLLXHBH = PLLXHBH;
    }

    public String getFKXX() {
        return FKXX;
    }

    public void setFKXX(String FKXX) {
        this.FKXX = FKXX;
    }

    public String getZRZJZHHM() {
        return ZRZJZHHM;
    }

    public void setZRZJZHHM(String ZRZHHM) {
        this.ZRZJZHHM = ZRZHHM;
    }

    public String getFKZH() {
        return FKZH;
    }

    public void setFKZH(String FKZH) {
        this.FKZH = FKZH;
    }

    public StCollectionPersonalBusinessDetails getGrywmx() {
        return grywmx;
    }

    public void setGrywmx(StCollectionPersonalBusinessDetails grywmx) {
        this.grywmx = grywmx;
    }

    public List<CCollectionAllochthounousTransferProcess> getProcesses() {
        return processes;
    }

    public void setProcesses(List<CCollectionAllochthounousTransferProcess> processes) {
        this.processes = processes;
    }

    public String getZCJGBH() {
        return ZCJGBH;
    }

    public void setZCJGBH(String ZCJGBH) {
        this.ZCJGBH = ZCJGBH;
    }

    public String getZCGJJZXMC() {
        return ZCGJJZXMC;
    }

    public void setZCGJJZXMC(String ZCGJJZXMC) {
        this.ZCGJJZXMC = ZCGJJZXMC;
    }

    public String getYGRZFGJJZH() {
        return YGRZFGJJZH;
    }

    public void setYGRZFGJJZH(String YGRZFGJJZH) {
        this.YGRZFGJJZH = YGRZFGJJZH;
    }

    public String getYGZDWMC() {
        return YGZDWMC;
    }

    public void setYGZDWMC(String YGZDWMC) {
        this.YGZDWMC = YGZDWMC;
    }

    public String getZJLX() {
        return ZJLX;
    }

    public void setZJLX(String ZJLX) {
        this.ZJLX = ZJLX;
    }

    public String getZJHM() {
        return ZJHM;
    }

    public void setZJHM(String ZJHM) {
        this.ZJHM = ZJHM;
    }

    public String getZGXM() {
        return ZGXM;
    }

    public void setZGXM(String ZGXM) {
        this.ZGXM = ZGXM;
    }

    public String getSJHM() {
        return SJHM;
    }

    public void setSJHM(String SJHM) {
        this.SJHM = SJHM;
    }

    public String getXZFGJJZH() {
        return XZFGJJZH;
    }

    public void setXZFGJJZH(String XZFGJJZH) {
        this.XZFGJJZH = XZFGJJZH;
    }

    public String getXGZDWMC() {
        return XGZDWMC;
    }

    public void setXGZDWMC(String XGZDWMC) {
        this.XGZDWMC = XGZDWMC;
    }

    public String getZRZJZHYHMC() {
        return ZRZJZHYHMC;
    }

    public void setZRZJZHYHMC(String ZRZHYHMC) {
        this.ZRZJZHYHMC = ZRZHYHMC;
    }

    public String getZRZJZH() {
        return ZRZJZH;
    }

    public void setZRZJZH(String ZRZH) {
        this.ZRZJZH = ZRZH;
    }

    public String getLXDHHCZ() {
        return LXDHHCZ;
    }

    public void setLXDHHCZ(String LXDHHCZ) {
        this.LXDHHCZ = LXDHHCZ;
    }

    public String getBLZL() {
        return BLZL;
    }

    public void setBLZL(String BLZL) {
        this.BLZL = BLZL;
    }

    public BigDecimal getYZHBJJE() {
        return YZHBJJE;
    }

    public void setYZHBJJE(BigDecimal YZHBJJE) {
        this.YZHBJJE = YZHBJJE;
    }

    public BigDecimal getBNDLX() {
        return BNDLX;
    }

    public void setBNDLX(BigDecimal BNDLX) {
        this.BNDLX = BNDLX;
    }

    public BigDecimal getZYJE() {
        return ZYJE;
    }

    public void setZYJE(BigDecimal ZYJE) {
        this.ZYJE = ZYJE;
    }

    public Date getKHRQ() {
        return KHRQ;
    }

    public void setKHRQ(Date KHRQ) {
        this.KHRQ = KHRQ;
    }

    public String getJZNY() {
        return JZNY;
    }

    public void setJZNY(String JZNY) {
        this.JZNY = JZNY;
    }

    public Boolean getLXJC() {
        return LXJC;
    }

    public void setLXJC(Boolean LXJC) {
        this.LXJC = LXJC;
    }

    public Integer getGJJDKCS() {
        return GJJDKCS;
    }

    public void setGJJDKCS(Integer GJJDKCS) {
        this.GJJDKCS = GJJDKCS;
    }

    public Boolean getJQDK() {
        return JQDK;
    }

    public void setJQDK(Boolean JQDK) {
        this.JQDK = JQDK;
    }

    public Boolean getSFCZPTPD() {
        return SFCZPTPD;
    }

    public void setSFCZPTPD(Boolean SFCZPTPD) {
        this.SFCZPTPD = SFCZPTPD;
    }

    public String getYWLXDH() {
        return YWLXDH;
    }

    public void setYWLXDH(String YWLXDH) {
        this.YWLXDH = YWLXDH;
    }

    public Date getLXDSCRQ() {
        return LXDSCRQ;
    }

    public void setLXDSCRQ(Date LXDSCRQ) {
        this.LXDSCRQ = LXDSCRQ;
    }

    public String getFKYHMC() {
        return FKYHMC;
    }

    public void setFKYHMC(String FKYHMC) {
        this.FKYHMC = FKYHMC;
    }

    public String getFKHM() {
        return FKHM;
    }

    public void setFKHM(String FKHM) {
        this.FKHM = FKHM;
    }

    public String getZRJGBH() {
        return ZRJGBH;
    }

    public void setZRJGBH(String ZRJGBH) {
        this.ZRJGBH = ZRJGBH;
    }

    public String getZRGJJZXMC() {
        return ZRGJJZXMC;
    }

    public void setZRGJJZXMC(String ZRZXMC) {
        this.ZRGJJZXMC = ZRZXMC;
    }

    public String getCZMC() {
        return CZMC;
    }

    public void setCZMC(String CZMC) {
        this.CZMC = CZMC;
    }

    public String getLXHBH() {
        return LXHBH;
    }

    public void setLXHBH(String LXHBH) {
        this.LXHBH = LXHBH;
    }

    public String getLXHZT() {
        return LXHZT;
    }

    public void setLXHZT(String LXHZT) {
        this.LXHZT = LXHZT;
    }
}
