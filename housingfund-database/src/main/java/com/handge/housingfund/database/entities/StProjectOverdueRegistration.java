package com.handge.housingfund.database.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.util.Date;

@Entity
@Table(name = "st_project_overdue_registration")
@org.hibernate.annotations.Table(appliesTo = "st_project_overdue_registration", comment = "项目贷款逾期登记信息 表7.0.7")
public class StProjectOverdueRegistration extends Common implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8424852193285742490L;
	@Column(name = "DKZH", columnDefinition = "VARCHAR(30) DEFAULT NULL COMMENT '贷款账号'")
	private String dkzh;
	@Column(name = "YQBJ", columnDefinition = "NUMERIC(18,2) DEFAULT NULL COMMENT '逾期本金'")
	private BigDecimal yqbj = BigDecimal.ZERO;
	@Column(name = "YQLX", columnDefinition = "NUMERIC(18,2) DEFAULT NULL COMMENT '逾期利息'")
	private BigDecimal yqlx = BigDecimal.ZERO;
	@Column(name = "YQFX", columnDefinition = "NUMERIC(18,2) DEFAULT NULL COMMENT '逾期罚息'")
	private BigDecimal yqfx = BigDecimal.ZERO;
	@Column(name = "SSRQ", columnDefinition = "DATETIME DEFAULT NULL COMMENT '实收日期'")
	private Date ssrq;
	@Column(name = "SSYQBJ", columnDefinition = "NUMERIC(18,2) DEFAULT NULL COMMENT '实收逾期本金'")
	private BigDecimal ssyqbj = BigDecimal.ZERO;
	@Column(name = "SSYQLX", columnDefinition = "NUMERIC(18,2) DEFAULT NULL COMMENT '实收逾期利息'")
	private BigDecimal ssyqlx = BigDecimal.ZERO;
	@Column(name = "SSYQFX", columnDefinition = "NUMERIC(18,2) DEFAULT NULL COMMENT '实收逾期罚息'")
	private BigDecimal ssyqfx = BigDecimal.ZERO;

	public StProjectOverdueRegistration(){
       super();


	}

	public StProjectOverdueRegistration(String id, Date created_at, Date updated_at, Date deleted_at, boolean deleted,
			String dkzh, BigDecimal yqbj, BigDecimal yqlx, BigDecimal yqfx, Date ssrq, BigDecimal ssyqbj,
			BigDecimal ssyqlx, BigDecimal ssyqfx) {
		this.id = id;
		this.created_at = created_at;
		this.updated_at = updated_at;
		this.deleted_at = deleted_at;
		this.deleted = deleted;
		this.dkzh = dkzh;
		this.yqbj = yqbj;
		this.yqlx = yqlx;
		this.yqfx = yqfx;
		this.ssrq = ssrq;
		this.ssyqbj = ssyqbj;
		this.ssyqlx = ssyqlx;
		this.ssyqfx = ssyqfx;
	}

	public String getDkzh() {
		return this.dkzh;
	}

	public void setDkzh(String dkzh) {
       this.updated_at = new Date();
		this.dkzh = dkzh;
	}

	public BigDecimal getYqbj() {
		return this.yqbj;
	}

	public void setYqbj(BigDecimal yqbj) {
       this.updated_at = new Date();
		this.yqbj = yqbj;
	}

	public BigDecimal getYqlx() {
		return this.yqlx;
	}

	public void setYqlx(BigDecimal yqlx) {
       this.updated_at = new Date();
		this.yqlx = yqlx;
	}

	public BigDecimal getYqfx() {
		return this.yqfx;
	}

	public void setYqfx(BigDecimal yqfx) {
       this.updated_at = new Date();
		this.yqfx = yqfx;
	}

	public Date getSsrq() {
		return this.ssrq;
	}

	public void setSsrq(Date ssrq) {
       this.updated_at = new Date();
		this.ssrq = ssrq;
	}

	public BigDecimal getSsyqbj() {
		return this.ssyqbj;
	}

	public void setSsyqbj(BigDecimal ssyqbj) {
       this.updated_at = new Date();
		this.ssyqbj = ssyqbj;
	}

	public BigDecimal getSsyqlx() {
		return this.ssyqlx;
	}

	public void setSsyqlx(BigDecimal ssyqlx) {
       this.updated_at = new Date();
		this.ssyqlx = ssyqlx;
	}

	public BigDecimal getSsyqfx() {
		return this.ssyqfx;
	}

	public void setSsyqfx(BigDecimal ssyqfx) {
       this.updated_at = new Date();
		this.ssyqfx = ssyqfx;
	}

}
