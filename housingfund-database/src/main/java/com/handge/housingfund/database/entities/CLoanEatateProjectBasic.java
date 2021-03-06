package com.handge.housingfund.database.entities;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

// default package
// Generated 2017-8-8 15:42:03 by Hibernate Tools 5.2.3.Final

/**
 * HEatateProjectBasic generated by hbm2java
 */
@Entity
@Table(name = "c_loan_eatate_project_basic")
@org.hibernate.annotations.Table(appliesTo = "c_loan_eatate_project_basic", comment = "楼盘项目")
public class CLoanEatateProjectBasic extends Common implements java.io.Serializable {

	private static final long serialVersionUID = 3214935146329765049L;
	@Column(name = "LPMC", columnDefinition = "VARCHAR(120) DEFAULT NULL COMMENT '楼盘名称'")
	private String lpmc;
	@Column(name = "LPBH", columnDefinition = "VARCHAR(120) DEFAULT NULL COMMENT '楼盘编号'")
	private String lpbh;

	@Column(name = "LPDZ", columnDefinition = "VARCHAR(255) DEFAULT NULL COMMENT '楼盘地址'")
	private String lpdz;
	@Column(name = "YSXKZH", columnDefinition = "VARCHAR(30) DEFAULT NULL COMMENT '预售许可证号'")
	private String ysxkzh;
	@Column(name = "HQTDDJ", columnDefinition = "NUMERIC(18,2) DEFAULT NULL COMMENT '获取土地单价'")
	private BigDecimal hqtddj = BigDecimal.ZERO;
	@Column(name = "HQTDZJ", columnDefinition = "NUMERIC(18,2) DEFAULT NULL COMMENT '获取土地总价'")
	private BigDecimal hqtdzj = BigDecimal.ZERO;
	@Column(name = "BZJBL", columnDefinition = "NUMERIC(4,2) DEFAULT NULL COMMENT '保证金比例'")
	private BigDecimal bzjbl = BigDecimal.ZERO;
	@Column(name = "JZZMJ", columnDefinition = "NUMERIC(18,2) DEFAULT NULL COMMENT '建筑总面积'")
	private BigDecimal jzzmj = BigDecimal.ZERO;
	@Column(name = "JZZJE", columnDefinition = "NUMERIC(18,2) DEFAULT NULL COMMENT '建筑总金额'")
	private BigDecimal jzzje = BigDecimal.ZERO;
	@Column(name = "AJXYRQ", columnDefinition = "DATETIME DEFAULT NULL COMMENT '按揭协议日期'")
	private Date ajxyrq;
	@Column(name = "LXR", columnDefinition = "VARCHAR(120) DEFAULT NULL COMMENT '联系人'")
	private String lxr;
	@Column(name = "LXDH", columnDefinition = "VARCHAR(20) DEFAULT NULL COMMENT '联系电话'")
	private String lxdh;
	@Column(name = "BeiZhu", columnDefinition = "TEXT DEFAULT NULL COMMENT '备注'")
	private String beiZhu;
	@Column(name = "BLZL", columnDefinition = "TEXT DEFAULT NULL COMMENT '办理资料'")
	private String blzl;
	@Column(name = "CZY", columnDefinition = "VARCHAR(120) DEFAULT NULL COMMENT '操作员'")
	private String czy;
	@Column(name = "YWWD", columnDefinition = "VARCHAR(255) DEFAULT NULL COMMENT '业务网点'")
	private String ywwd;
	@Column(name = "SFBG", columnDefinition = "BIT(1) DEFAULT NULL COMMENT '是否变更'")
	private Boolean sfbg;
	@Column(name = "SFQY", columnDefinition = "BIT(1) DEFAULT NULL COMMENT '是否启用'")
	private Boolean sfqy;

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "cLoanEatateProjectBasic")
	private List<CLoanBuildingInformationBasic> HBuildingInformationBasics;

	@ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@JoinColumn(name = "cLoanHousingCompanyBasic", columnDefinition = "VARCHAR(32) DEFAULT NULL COMMENT '公司贷款基础表'")
	private CLoanHousingCompanyBasic cLoanHousingCompanyBasic;

	public CLoanEatateProjectBasic() {
		super();
	}

	public CLoanEatateProjectBasic(String id, Date created_at, boolean deleted, Date deleted_at, Date updated_at,
			String lpmc, String lpdz, String ysxkzh, BigDecimal hqtddj, BigDecimal hqtdzj, BigDecimal bzjbl,
			BigDecimal jzzmj, BigDecimal jzzje, Date ajxyrq, String lxr, String lxdh, String beiZhu, String blzl,
			Boolean sfbg, String czy, String ywwd, List<CLoanBuildingInformationBasic> HBuildingInformationBasics) {
		this.id = id;
		this.created_at = created_at;
		this.deleted = deleted;
		this.deleted_at = deleted_at;
		this.updated_at = updated_at;
		this.lpmc = lpmc;
		this.lpdz = lpdz;
		this.ysxkzh = ysxkzh;
		this.hqtddj = hqtddj;
		this.hqtdzj = hqtdzj;
		this.czy = czy;
		this.ywwd = ywwd;
		this.bzjbl = bzjbl;
		this.jzzmj = jzzmj;
		this.jzzje = jzzje;
		this.ajxyrq = ajxyrq;
		this.lxr = lxr;
		this.lxdh = lxdh;
		this.beiZhu = beiZhu;
		this.blzl = blzl;
		this.sfbg = sfbg;
		this.HBuildingInformationBasics = HBuildingInformationBasics;
	}

	public String getLpmc() {
		return lpmc;
	}

	public void setLpmc(String lpmc) {
		this.updated_at = new Date();
		this.lpmc = lpmc;
	}

	public String getLpbh() {
		return lpbh;
	}

	public void setLpbh(String lpbh) {
		this.updated_at = new Date();
		this.lpbh = lpbh;
	}

	public String getLpdz() {
		return lpdz;
	}

	public void setLpdz(String lpdz) {
		this.updated_at = new Date();
		this.lpdz = lpdz;
	}

	public String getYsxkzh() {
		return ysxkzh;
	}

	public void setYsxkzh(String ysxkzh) {
		this.updated_at = new Date();
		this.ysxkzh = ysxkzh;
	}

	public BigDecimal getHqtddj() {
		return hqtddj;
	}

	public void setHqtddj(BigDecimal hqtddj) {
		this.updated_at = new Date();
		this.hqtddj = hqtddj;
	}

	public BigDecimal getHqtdzj() {
		return hqtdzj;
	}

	public void setHqtdzj(BigDecimal hqtdzj) {
		this.updated_at = new Date();
		this.hqtdzj = hqtdzj;
	}

	public BigDecimal getBzjbl() {
		return bzjbl;
	}

	public void setBzjbl(BigDecimal bzjbl) {
		this.updated_at = new Date();
		this.bzjbl = bzjbl;
	}

	public BigDecimal getJzzmj() {
		return jzzmj;
	}

	public void setJzzmj(BigDecimal jzzmj) {
		this.updated_at = new Date();
		this.jzzmj = jzzmj;
	}

	public BigDecimal getJzzje() {
		return jzzje;
	}

	public void setJzzje(BigDecimal jzzje) {
		this.updated_at = new Date();
		this.jzzje = jzzje;
	}

	public Date getAjxyrq() {
		return ajxyrq;
	}

	public void setAjxyrq(Date ajxyrq) {
		this.updated_at = new Date();
		this.ajxyrq = ajxyrq;
	}

	public String getLxr() {
		return lxr;
	}

	public void setLxr(String lxr) {
		this.updated_at = new Date();
		this.lxr = lxr;
	}

	public String getLxdh() {
		return lxdh;
	}

	public void setLxdh(String lxdh) {
		this.updated_at = new Date();
		this.lxdh = lxdh;
	}

	public String getBeiZhu() {
		return beiZhu;
	}

	public void setBeiZhu(String beiZhu) {
		this.updated_at = new Date();
		this.beiZhu = beiZhu;
	}

	public String getBlzl() {
		return blzl;
	}

	public void setBlzl(String blzl) {
		this.updated_at = new Date();
		this.blzl = blzl;
	}

	public String getCzy() {
		return czy;
	}

	public void setCzy(String czy) {
		this.updated_at = new Date();
		this.czy = czy;
	}

	public String getYwwd() {
		return ywwd;
	}

	public void setYwwd(String ywwd) {
		this.updated_at = new Date();
		this.ywwd = ywwd;
	}

	public Boolean getSfbg() {
		return sfbg;
	}

	public void setSfbg(Boolean sfbg) {
		this.updated_at = new Date();
		this.sfbg = sfbg;
	}

	public Boolean getSfqy() {
		return sfqy;
	}

	public void setSfqy(Boolean sfqy) {
		this.updated_at = new Date();
		this.sfqy = sfqy;
	}

	public List<CLoanBuildingInformationBasic> getHBuildingInformationBasics() {
		return HBuildingInformationBasics;
	}

	public void setHBuildingInformationBasics(List<CLoanBuildingInformationBasic> hBuildingInformationBasics) {
		this.updated_at = new Date();
		HBuildingInformationBasics = hBuildingInformationBasics;
	}

	public CLoanHousingCompanyBasic getcLoanHousingCompanyBasic() {
		return cLoanHousingCompanyBasic;
	}

	public void setcLoanHousingCompanyBasic(CLoanHousingCompanyBasic cLoanHousingCompanyBasic) {
		this.updated_at = new Date();
		this.cLoanHousingCompanyBasic = cLoanHousingCompanyBasic;
	}

}
