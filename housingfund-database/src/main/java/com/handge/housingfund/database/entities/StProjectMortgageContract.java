package com.handge.housingfund.database.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.util.Date;

@Entity
@Table(name = "st_project_mortgage_contract")
@org.hibernate.annotations.Table(appliesTo = "st_project_mortgage_contract", comment = "项目贷款抵押合同信息 表7.0.4")
public class StProjectMortgageContract extends Common implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1830764729606523078L;
	@Column(name = "DYHTBH", columnDefinition = "VARCHAR(20) DEFAULT NULL COMMENT '抵押合同编号'")
	private String dyhtbh;
	@Column(name = "JKHTBH", columnDefinition = "VARCHAR(30) DEFAULT NULL COMMENT '借款合同编号'")
	private String jkhtbh;
	@Column(name = "DYWLX", columnDefinition = "VARCHAR(2) DEFAULT NULL COMMENT '抵押物类型'")
	private String dywlx;
	@Column(name = "DYWMC", columnDefinition = "VARCHAR(255) DEFAULT NULL COMMENT '抵押物名称'")
	private String dywmc;
	@Column(name = "DYWQZH", columnDefinition = "VARCHAR(30) DEFAULT NULL COMMENT '抵押物权证号'")
	private String dywqzh;
	@Column(name = "DYWCS", columnDefinition = "VARCHAR(255) DEFAULT NULL COMMENT '抵押物处所'")
	private String dywcs;
	@Column(name = "DYQJLRQ", columnDefinition = "DATETIME DEFAULT NULL COMMENT '抵押权建立日期'")
	private Date dyqjlrq;
	@Column(name = "DYQJCRQ", columnDefinition = "DATETIME DEFAULT NULL COMMENT '抵押权解除日期'")
	private Date dyqjcrq;
	@Column(name = "DYWPGJZ", columnDefinition = "NUMERIC(18,2) DEFAULT NULL COMMENT '抵押物评估价值'")
	private BigDecimal dywpgjz = BigDecimal.ZERO;
	@Column(name = "YDYJZ", columnDefinition = "NUMERIC(18,2) DEFAULT NULL COMMENT '已抵押价值'")
	private BigDecimal ydyjz = BigDecimal.ZERO;
	@Column(name = "DYL", columnDefinition = "NUMERIC(3,2) DEFAULT NULL COMMENT '抵押率'")
	private BigDecimal dyl = BigDecimal.ZERO;

	public StProjectMortgageContract(){
       super();


	}

	public StProjectMortgageContract(String id, Date created_at, Date updated_at, Date deleted_at, boolean deleted,
			String dyhtbh, String jkhtbh, String dywlx, String dywmc, String dywqzh, String dywcs, Date dyqjlrq,
			Date dyqjcrq, BigDecimal dywpgjz, BigDecimal ydyjz, BigDecimal dyl) {
		this.id = id;
		this.created_at = created_at;
		this.updated_at = updated_at;
		this.deleted_at = deleted_at;
		this.deleted = deleted;
		this.dyhtbh = dyhtbh;
		this.jkhtbh = jkhtbh;
		this.dywlx = dywlx;
		this.dywmc = dywmc;
		this.dywqzh = dywqzh;
		this.dywcs = dywcs;
		this.dyqjlrq = dyqjlrq;
		this.dyqjcrq = dyqjcrq;
		this.dywpgjz = dywpgjz;
		this.ydyjz = ydyjz;
		this.dyl = dyl;
	}

	public String getDyhtbh() {
		return this.dyhtbh;
	}

	public void setDyhtbh(String dyhtbh) {
       this.updated_at = new Date();
		this.dyhtbh = dyhtbh;
	}

	public String getJkhtbh() {
		return this.jkhtbh;
	}

	public void setJkhtbh(String jkhtbh) {
       this.updated_at = new Date();
		this.jkhtbh = jkhtbh;
	}

	public String getDywlx() {
		return this.dywlx;
	}

	public void setDywlx(String dywlx) {
       this.updated_at = new Date();
		this.dywlx = dywlx;
	}

	public String getDywmc() {
		return this.dywmc;
	}

	public void setDywmc(String dywmc) {
       this.updated_at = new Date();
		this.dywmc = dywmc;
	}

	public String getDywqzh() {
		return this.dywqzh;
	}

	public void setDywqzh(String dywqzh) {
       this.updated_at = new Date();
		this.dywqzh = dywqzh;
	}

	public String getDywcs() {
		return this.dywcs;
	}

	public void setDywcs(String dywcs) {
       this.updated_at = new Date();
		this.dywcs = dywcs;
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

	public BigDecimal getYdyjz() {
		return this.ydyjz;
	}

	public void setYdyjz(BigDecimal ydyjz) {
       this.updated_at = new Date();
		this.ydyjz = ydyjz;
	}

	public BigDecimal getDyl() {
		return this.dyl;
	}

	public void setDyl(BigDecimal dyl) {
       this.updated_at = new Date();
		this.dyl = dyl;
	}

}
