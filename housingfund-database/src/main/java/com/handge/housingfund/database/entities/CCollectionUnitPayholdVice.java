package com.handge.housingfund.database.entities;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by 凡 on 2017/7/21. 单位缓缴
 */
@Entity
@Table(name = "c_collection_unit_payhold_vice")
@org.hibernate.annotations.Table(appliesTo = "c_collection_unit_payhold_vice", comment = "单位缓缴")
public class CCollectionUnitPayholdVice extends Common implements Serializable {

	private static final long serialVersionUID = -6884358662596970170L;
	@Column(name = "HJKSSJ", columnDefinition = "DATETIME DEFAULT NULL COMMENT '缓缴开始时间'")
	private Date hjfssj;
	@Column(name = "HJJSSJ", columnDefinition = "DATETIME DEFAULT NULL COMMENT '缓缴结束时间'")
	private Date hjjssj;
	@Column(name = "QUNIANKS", columnDefinition = "NUMERIC(18,2) DEFAULT NULL COMMENT '去年亏损'")
	private BigDecimal quNianKS = BigDecimal.ZERO;
	@Column(name = "QIANNIANKS", columnDefinition = "NUMERIC(18,2) DEFAULT NULL COMMENT '前年亏损'")
	private BigDecimal qianNianKS = BigDecimal.ZERO;
	@Column(name = "QUNIANYL", columnDefinition = "NUMERIC(18,2) DEFAULT NULL COMMENT '去年盈利'")
	private BigDecimal quNianYL = BigDecimal.ZERO;
	@Column(name = "QIANNIANYL", columnDefinition = "NUMERIC(18,2) DEFAULT NULL COMMENT '前年盈利'")
	private BigDecimal qianNianYL = BigDecimal.ZERO;
	@Column(name = "QUNIANRJGZ", columnDefinition = "NUMERIC(18,2) DEFAULT NULL COMMENT '去年人均工资'")
	private BigDecimal quNianRJGZ = BigDecimal.ZERO;
	@Column(name = "QIANNIANRJGZ", columnDefinition = "NUMERIC(18,2) DEFAULT NULL COMMENT '前年人均工资'")
	private BigDecimal qianNianRJGZ = BigDecimal.ZERO;

	@OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@JoinColumn(name = "DWYWMX", columnDefinition = "varchar(32) DEFAULT NULL COMMENT '单位业务明细'")
	private StCollectionUnitBusinessDetails dwywmx;

	public Date getHjfssj() {
		return hjfssj;
	}

	public void setHjfssj(Date hjfssj) {
		this.updated_at = new Date();
		this.hjfssj = hjfssj;
	}

	public Date getHjjssj() {
		return hjjssj;
	}

	public void setHjjssj(Date hjjssj) {
		this.updated_at = new Date();
		this.hjjssj = hjjssj;
	}

	public BigDecimal getQuNianKS() {
		return quNianKS;
	}

	public void setQuNianKS(BigDecimal quNianKS) {
		this.updated_at = new Date();
		this.quNianKS = quNianKS;
	}

	public BigDecimal getQianNianKS() {
		return qianNianKS;
	}

	public void setQianNianKS(BigDecimal qianNianKS) {
		this.updated_at = new Date();
		this.qianNianKS = qianNianKS;
	}

	public BigDecimal getQuNianYL() {
		return quNianYL;
	}

	public void setQuNianYL(BigDecimal quNianYL) {
		this.updated_at = new Date();
		this.quNianYL = quNianYL;
	}

	public BigDecimal getQianNianYL() {
		return qianNianYL;
	}

	public void setQianNianYL(BigDecimal qianNianYL) {
		this.updated_at = new Date();
		this.qianNianYL = qianNianYL;
	}

	public BigDecimal getQuNianRJGZ() {
		return quNianRJGZ;
	}

	public void setQuNianRJGZ(BigDecimal quNianRJGZ) {
		this.updated_at = new Date();
		this.quNianRJGZ = quNianRJGZ;
	}

	public BigDecimal getQianNianRJGZ() {
		return qianNianRJGZ;
	}

	public void setQianNianRJGZ(BigDecimal qianNianRJGZ) {
		this.updated_at = new Date();
		this.qianNianRJGZ = qianNianRJGZ;
	}

	public StCollectionUnitBusinessDetails getDwywmx() {
		return dwywmx;
	}

	public void setDwywmx(StCollectionUnitBusinessDetails dwywmx) {
		this.updated_at = new Date();
		this.dwywmx = dwywmx;
	}

	public CCollectionUnitPayholdVice() {
		super();

	}

	public CCollectionUnitPayholdVice(String id, Date created_at, Date updated_at, Date deleted_at, boolean deleted,
			Date hjfssj, Date hjjssj, BigDecimal quNianKS, BigDecimal qianNianKS, BigDecimal quNianYL,
			BigDecimal qianNianYL, BigDecimal quNianRJGZ, BigDecimal qianNianRJGZ,
			StCollectionUnitBusinessDetails dwywmx) {
		this.id = id;
		this.created_at = created_at;
		this.updated_at = updated_at;
		this.deleted_at = deleted_at;
		this.deleted = deleted;
		this.hjfssj = hjfssj;
		this.hjjssj = hjjssj;
		this.quNianKS = quNianKS;
		this.qianNianKS = qianNianKS;
		this.quNianYL = quNianYL;
		this.qianNianYL = qianNianYL;
		this.quNianRJGZ = quNianRJGZ;
		this.qianNianRJGZ = qianNianRJGZ;
		this.dwywmx = dwywmx;
	}
}
