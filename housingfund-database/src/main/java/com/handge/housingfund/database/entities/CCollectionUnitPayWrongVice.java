package com.handge.housingfund.database.entities;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Set;

/**
 * Created by 凡 on 2017/7/21. 单位错缴
 */
@Entity
@Table(name = "c_collection_unit_paywrong_vice")
@org.hibernate.annotations.Table(appliesTo = "c_collection_unit_paywrong_vice", comment = "单位错缴")
public class CCollectionUnitPayWrongVice extends Common implements Serializable {

	private static final long serialVersionUID = 7874093351981050561L;
	// TODO 清册确认单号长度/规则确认
	@Column(name = "QCQRDH", columnDefinition = "VARCHAR(20) DEFAULT NULL COMMENT '清册确认单号'")
	private String qcqrdh;
	@Column(name = "JCGZNY", columnDefinition = "DATETIME DEFAULT NULL COMMENT '缴存更正年月'")
	private Date jcgzny;
	@Column(name = "SKYHZH", columnDefinition = "VARCHAR(30) DEFAULT NULL COMMENT '收款银行账户'")
	private String skyhzh;
	@Column(name = "SKYHHM", columnDefinition = "VARCHAR(120) DEFAULT NULL COMMENT '收款银行户名'")
	private String skyhhm;
	@Column(name = "SKYHDM", columnDefinition = "VARCHAR(3) DEFAULT NULL COMMENT '收款银行代码'")
	private String skyhdm;
	@Column(name = "SKYHMC", columnDefinition = "VARCHAR(250) DEFAULT NULL COMMENT '收款银行名称'")
	private String skyhmc;

	@OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@JoinColumn(name = "DWYWMX", columnDefinition = "VARCHAR(32) DEFAULT NULL COMMENT '单位业务明细'")
	private StCollectionUnitBusinessDetails dwywmx;

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@JoinColumn(name = "CJXQ", columnDefinition = "VARCHAR(32) DEFAULT NULL COMMENT '错缴详情'")
	private Set<CCollectionUnitPayWrongDetailVice> cjxq;

	@Column(name = "FSE", columnDefinition = "NUMERIC(18,2) DEFAULT NULL COMMENT '发生额'")
	private BigDecimal fse = BigDecimal.ZERO;

	public BigDecimal getFse() {
		return fse;
	}

	public void setFse(BigDecimal fse) {
		this.fse = fse;
	}

	public String getQcqrdh() {
		return qcqrdh;
	}

	public void setQcqrdh(String qcqrdh) {
		this.updated_at = new Date();
		this.qcqrdh = qcqrdh;
	}

	public Date getJcgzny() {
		return jcgzny;
	}

	public void setJcgzny(Date jcgzny) {
		this.updated_at = new Date();
		this.jcgzny = jcgzny;
	}

	public String getSkyhzh() {
		return skyhzh;
	}

	public void setSkyhzh(String skyhzh) {
		this.updated_at = new Date();
		this.skyhzh = skyhzh;
	}

	public String getSkyhhm() {
		return skyhhm;
	}

	public void setSkyhhm(String skyhhm) {
		this.updated_at = new Date();
		this.skyhhm = skyhhm;
	}

	public String getSkyhdm() {
		return skyhdm;
	}

	public void setSkyhdm(String skyhdm) {
		this.updated_at = new Date();
		this.skyhdm = skyhdm;
	}

	public StCollectionUnitBusinessDetails getDwywmx() {
		return dwywmx;
	}

	public void setDwywmx(StCollectionUnitBusinessDetails dwywmx) {
		this.updated_at = new Date();
		this.dwywmx = dwywmx;
	}

	public Set<CCollectionUnitPayWrongDetailVice> getCjxq() {
		return cjxq;
	}

	public void setCjxq(Set<CCollectionUnitPayWrongDetailVice> cjxq) {
		this.updated_at = new Date();
		this.cjxq = cjxq;
	}

	public String getSkyhmc() {
		return skyhmc;
	}

	public void setSkyhmc(String skyhmc) {
		this.updated_at = new Date();
		this.skyhmc = skyhmc;
	}

	public CCollectionUnitPayWrongVice() {
		super();

	}

	public CCollectionUnitPayWrongVice(String id, Date created_at, Date updated_at, Date deleted_at, boolean deleted,
			String qcqrdh, Date jcgzny, String skyhzh, String skyhhm, String skyhdm, String skyhmc,
			StCollectionUnitBusinessDetails dwywmx, Set<CCollectionUnitPayWrongDetailVice> cjxq, BigDecimal fse) {
		this.id = id;
		this.created_at = created_at;
		this.updated_at = updated_at;
		this.deleted_at = deleted_at;
		this.deleted = deleted;
		this.qcqrdh = qcqrdh;
		this.jcgzny = jcgzny;
		this.skyhzh = skyhzh;
		this.skyhhm = skyhhm;
		this.skyhdm = skyhdm;
		this.dwywmx = dwywmx;
		this.cjxq = cjxq;
		this.skyhmc = skyhmc;
		this.fse = fse;
	}
}
