package com.handge.housingfund.database.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Date;

/**
 * Created by tanyi on 2017/8/23.
 */
@Entity
@Table(name = "c_finance_daily_business_base")
@org.hibernate.annotations.Table(appliesTo = "c_finance_daily_business_base", comment = "日常业务处理 基础表")
public class CFinanceDailyBusinessBase extends Common implements java.io.Serializable {
	private static final long serialVersionUID = 800984849378801494L;
	@Column(name = "ZJYWLX", columnDefinition = "VARCHAR(255) DEFAULT NULL COMMENT '资金业务类型'")
	private String zjywlx;
	@Column(name = "YWSJ", columnDefinition = "TEXT DEFAULT NULL COMMENT '业务数据json'")
	private String ywsj;
	@Column(name = "BLZL", columnDefinition = "TEXT DEFAULT NULL COMMENT '办理资料'")
	private String blzl;
	@Column(name = "CZY", columnDefinition = "VARCHAR(120) DEFAULT NULL COMMENT '操作员'")
	private String czy;
	@Column(name = "SLSJ", columnDefinition = "DATETIME DEFAULT NULL COMMENT '受理时间'")
	private Date slsj;

	@Column(name = "YWLSH", columnDefinition = "VARCHAR(20) DEFAULT NULL COMMENT '业务流水号'")
	private String ywlsh;

	public CFinanceDailyBusinessBase() {
		super();

	}

	public CFinanceDailyBusinessBase(String id, Date created_at, Date updated_at, Date deleted_at, Boolean deleted,
			String zjywlx, String ywsj, String blzl, String czy, Date slsj, String ywlsh) {
		this.id = id;
		this.created_at = created_at;
		this.updated_at = updated_at;
		this.deleted_at = deleted_at;
		this.deleted = deleted;
		this.zjywlx = zjywlx;
		this.ywsj = ywsj;
		this.blzl = blzl;
		this.czy = czy;
		this.slsj = slsj;
		this.ywlsh = ywlsh;
	}

	public String getYwlsh() {
		return ywlsh;
	}

	public void setYwlsh(String ywlsh) {
		this.updated_at = new Date();
		this.ywlsh = ywlsh;
	}

	public String getCzy() {
		return czy;
	}

	public void setCzy(String czy) {
		this.updated_at = new Date();
		this.czy = czy;
	}

	public Date getSlsj() {
		return slsj;
	}

	public void setSlsj(Date slsj) {
		this.updated_at = new Date();
		this.slsj = slsj;
	}

	public String getZjywlx() {
		return zjywlx;
	}

	public void setZjywlx(String zjywlx) {
		this.updated_at = new Date();
		this.zjywlx = zjywlx;
	}

	public String getYwsj() {
		return ywsj;
	}

	public void setYwsj(String ywsj) {
		this.updated_at = new Date();
		this.ywsj = ywsj;
	}

	public String getBlzl() {
		return blzl;
	}

	public void setBlzl(String blzl) {
		this.updated_at = new Date();
		this.blzl = blzl;
	}
}
