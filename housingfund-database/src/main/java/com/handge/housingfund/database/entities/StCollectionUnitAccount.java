package com.handge.housingfund.database.entities;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

@Entity
@Table(name = "st_collection_unit_account", indexes = {
		@Index(name = "INDEX_DWZH", columnList = "DWZH", unique = true) })
@org.hibernate.annotations.Table(appliesTo = "st_collection_unit_account", comment = "单位账户信息 表5.0.2")
public class StCollectionUnitAccount extends Common implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 857353810618197933L;
	@Column(name = "DWZH", columnDefinition = "VARCHAR(20) DEFAULT NULL COMMENT '单位账号'")
	private String dwzh;
	@Column(name = "DWJCBL", columnDefinition = "NUMERIC(4,2) DEFAULT NULL COMMENT '单位缴存比例'")
	private BigDecimal dwjcbl = BigDecimal.ZERO;
	@Column(name = "GRJCBL", columnDefinition = "NUMERIC(4,2) DEFAULT NULL COMMENT '个人缴存比例'")
	private BigDecimal grjcbl = BigDecimal.ZERO;
	@Column(name = "DWJCRS", columnDefinition = "NUMERIC(18,0) DEFAULT NULL COMMENT '单位缴存人数'")
	private Long dwjcrs;
	@Column(name = "DWFCRS", columnDefinition = "NUMERIC(18,0) DEFAULT NULL COMMENT '单位封存人数'")
	private Long dwfcrs;
	@Column(name = "DWZHYE", columnDefinition = "NUMERIC(18,2) DEFAULT NULL COMMENT '单位账户余额'")
	private BigDecimal dwzhye = BigDecimal.ZERO;
	@Column(name = "DWXHRQ", columnDefinition = "DATETIME DEFAULT NULL COMMENT '单位销户日期'")
	private Date dwxhrq;
	@Column(name = "DWXHYY", columnDefinition = "VARCHAR(2) DEFAULT NULL COMMENT '单位销户原因'")
	private String dwxhyy;
	@Column(name = "DWZHZT", columnDefinition = "VARCHAR(2) DEFAULT NULL COMMENT '单位账户状态'")
	private String dwzhzt;
	@Column(name = "JZNY", columnDefinition = "VARCHAR(6) DEFAULT NULL COMMENT '缴至年月'")
	private String jzny;
	@OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@JoinColumn(name = "extenstion", columnDefinition = "VARCHAR(32) DEFAULT NULL COMMENT '缴存单位账号扩展'")
	private CCollectionUnitAccountExtension cCollectionUnitAccountExtension;

	public StCollectionUnitAccount() {
		super();

	}

	public StCollectionUnitAccount(String id, Date created_at, Date updated_at, Date deleted_at, boolean deleted,
			String dwzh, BigDecimal dwjcbl, BigDecimal grjcbl, Long dwjcrs, Long dwfcrs, BigDecimal dwzhye, Date dwxhrq,
			String dwxhyy, String dwzhzt, String jzny, CCollectionUnitAccountExtension extension) {
		this.id = id;
		this.created_at = created_at;
		this.updated_at = updated_at;
		this.deleted_at = deleted_at;
		this.deleted = deleted;
		this.dwzh = dwzh;
		this.dwjcbl = dwjcbl;
		this.grjcbl = grjcbl;
		this.dwjcrs = dwjcrs;
		this.dwfcrs = dwfcrs;
		this.dwzhye = dwzhye;
		this.dwxhrq = dwxhrq;
		this.dwxhyy = dwxhyy;
		this.dwzhzt = dwzhzt;
		this.jzny = jzny;
		this.cCollectionUnitAccountExtension = extension;
	}

	public String getDwzh() {
		return this.dwzh;
	}

	public void setDwzh(String dwzh) {
		this.updated_at = new Date();
		this.dwzh = dwzh;
	}

	public BigDecimal getDwjcbl() {
		return this.dwjcbl;
	}

	public void setDwjcbl(BigDecimal dwjcbl) {
		this.updated_at = new Date();
		this.dwjcbl = dwjcbl;
	}

	public BigDecimal getGrjcbl() {
		return this.grjcbl;
	}

	public void setGrjcbl(BigDecimal grjcbl) {
		this.updated_at = new Date();
		this.grjcbl = grjcbl;
	}

	public Long getDwjcrs() {
		return this.dwjcrs;
	}

	public void setDwjcrs(Long dwjcrs) {
		this.updated_at = new Date();
		this.dwjcrs = dwjcrs;
	}

	public Long getDwfcrs() {
		return this.dwfcrs;
	}

	public void setDwfcrs(Long dwfcrs) {
		this.updated_at = new Date();
		this.dwfcrs = dwfcrs;
	}

	public BigDecimal getDwzhye() {
		return this.dwzhye;
	}

	public void setDwzhye(BigDecimal dwzhye) {
		this.updated_at = new Date();
		this.dwzhye = dwzhye;
	}

	public Date getDwxhrq() {
		return this.dwxhrq;
	}

	public void setDwxhrq(Date dwxhrq) {
		this.updated_at = new Date();
		this.dwxhrq = dwxhrq;
	}

	public String getDwxhyy() {
		return this.dwxhyy;
	}

	public CCollectionUnitAccountExtension getExtension() {
		return cCollectionUnitAccountExtension;
	}

	public void setExtension(CCollectionUnitAccountExtension extension) {
		this.updated_at = new Date();
		this.cCollectionUnitAccountExtension = extension;
	}

	public void setDwxhyy(String dwxhyy) {
		this.updated_at = new Date();
		this.dwxhyy = dwxhyy;
	}

	public String getDwzhzt() {
		return this.dwzhzt;
	}

	public void setDwzhzt(String dwzhzt) {
		this.updated_at = new Date();
		this.dwzhzt = dwzhzt;
	}

	public String getJzny() {
		return this.jzny;
	}

	public void setJzny(String jzny) {
		this.updated_at = new Date();
		this.jzny = jzny;
	}

}
