package com.handge.housingfund.database.entities;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Set;

/**
 * Created by 凡 on 2017/7/20. 单位汇缴
 */
@Entity
@Table(name = "c_collection_unit_remittance_vice")
@org.hibernate.annotations.Table(appliesTo = "c_collection_unit_remittance_vice", comment = "单位汇缴")
public class CCollectionUnitRemittanceVice extends Common implements Serializable {

	private static final long serialVersionUID = 3477406058642631168L;
	// TODO 清册确认单号长度/规则确认
	@Column(name = "QCQRDH", columnDefinition = "VARCHAR(20) DEFAULT NULL COMMENT '清册确认单号'")
	private String qcqrdh;
	@Column(name = "HBJNY", columnDefinition = "VARCHAR(6) DEFAULT NULL COMMENT '汇补缴年月'")
	private String hbjny;
	// TODO 类型
	@Column(name = "HJFS", columnDefinition = "VARCHAR(20) DEFAULT NULL COMMENT '汇缴方式'")
	private String hjfs;
	@Column(name = "STYHZH", columnDefinition = "VARCHAR(30) DEFAULT NULL COMMENT '受托银行账户'")
	private String styhzh;
	@Column(name = "STYHMC", columnDefinition = "VARCHAR(255) DEFAULT NULL COMMENT '受托银行名称'")
	private String styhmc;
	@Column(name = "STYHDM", columnDefinition = "VARCHAR(3) DEFAULT NULL COMMENT '受托银行代码'")
	private String styhdm;

	@OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@JoinColumn(name = "DWYWMX", columnDefinition = "VARCHAR(32) DEFAULT NULL COMMENT '单位业务明细'")
	private StCollectionUnitBusinessDetails dwywmx;

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@JoinColumn(name = "HJXQ", columnDefinition = "VARCHAR(32) DEFAULT NULL COMMENT '汇缴详情'")
	private Set<CCollectionUnitRemittanceDetailVice> hjxq;

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

	public String getHbjny() {
		return hbjny;
	}

	public void setHbjny(String hbjny) {
		this.updated_at = new Date();
		this.hbjny = hbjny;
	}

	public String getHjfs() {
		return hjfs;
	}

	public void setHjfs(String hjfs) {
		this.updated_at = new Date();
		this.hjfs = hjfs;
	}

	public String getStyhzh() {
		return styhzh;
	}

	public void setStyhzh(String styhzh) {
		this.updated_at = new Date();
		this.styhzh = styhzh;
	}

	public String getStyhmc() {
		return styhmc;
	}

	public void setStyhmc(String styhmc) {
		this.updated_at = new Date();
		this.styhmc = styhmc;
	}

	public String getStyhdm() {
		return styhdm;
	}

	public void setStyhdm(String styhdm) {
		this.updated_at = new Date();
		this.styhdm = styhdm;
	}

	public StCollectionUnitBusinessDetails getDwywmx() {
		return dwywmx;
	}

	public void setDwywmx(StCollectionUnitBusinessDetails dwywmx) {
		this.updated_at = new Date();
		this.dwywmx = dwywmx;
	}

	public Set<CCollectionUnitRemittanceDetailVice> getHjxq() {
		return hjxq;
	}

	public void setHjxq(Set<CCollectionUnitRemittanceDetailVice> hjxq) {
		this.updated_at = new Date();
		this.hjxq = hjxq;
	}

	public CCollectionUnitRemittanceVice() {
		super();

	}

	public CCollectionUnitRemittanceVice(String id, Date created_at, Date updated_at, Date deleted_at, boolean deleted,
			String ywlsh, String qcqrdh, String hbjny, String hjfs, String styhzh, String styhmc, String styhdm,
			StCollectionUnitBusinessDetails dwywmx, Set<CCollectionUnitRemittanceDetailVice> hjxq, BigDecimal fse) {
		this.id = id;
		this.created_at = created_at;
		this.updated_at = updated_at;
		this.deleted_at = deleted_at;
		this.deleted = deleted;
		this.qcqrdh = qcqrdh;
		this.hbjny = hbjny;
		this.hjfs = hjfs;
		this.styhzh = styhzh;
		this.styhmc = styhmc;
		this.styhdm = styhdm;
		this.dwywmx = dwywmx;
		this.hjxq = hjxq;
		this.fse = fse;
	}
}
