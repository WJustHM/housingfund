package com.handge.housingfund.database.entities;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * Created by Liujuhao on 2017/7/10.
 */
@Entity
@Table(name = "c_collection_individual_account_merge_vice")
@org.hibernate.annotations.Table(appliesTo = "c_collection_individual_account_merge_vice", comment = "针对“合并个人账户”")
public class CCollectionIndividualAccountMergeVice extends Common implements Serializable {
	private static final long serialVersionUID = -2872971101920521362L;
	@Column(name = "YWLSH", columnDefinition = "VARCHAR(20) DEFAULT NULL COMMENT '业务流水号'")
	private String ywlsh;
	@Column(name = "GRZH", columnDefinition = "VARCHAR(20) DEFAULT NULL COMMENT '个人账号'")
	private String grzh;
	@Column(name = "DWZH", columnDefinition = "VARCHAR(20) DEFAULT NULL COMMENT '单位账号'")
	private String dwzh;
	// @Column(name = "YWZT", columnDefinition = "char(2) DEFAULT NULL COMMENT
	// '业务状态'")
	// private String ywzt;
	// @Column(name = "STEP", columnDefinition = "char(2) DEFAULT NULL COMMENT
	// '审核状态'")
	// private String step;
	@Column(name = "BLZL", columnDefinition = "TEXT DEFAULT NULL COMMENT '办理资料'")
	private String blzl;
	@Column(name = "CZMC", columnDefinition = "VARCHAR(2) DEFAULT NULL COMMENT '操作名称'")
	private String czmc;
	@Column(name = "SLSJ", columnDefinition = "DATETIME DEFAULT NULL COMMENT '受理时间'")
	private Date slsj;
	@Column(name = "CZY", columnDefinition = "VARCHAR(120) DEFAULT NULL COMMENT '操作员'")
	private String czy;
	@Column(name = "YWWD", columnDefinition = "VARCHAR(255) DEFAULT NULL COMMENT '业务网点'")
	private String ywwd;
	@Column(name = "BLZH", columnDefinition = "VARCHAR(20) DEFAULT NULL COMMENT '保留账号（仅合并业务）'")
	private String blzh;
	@Column(name = "HBZH", columnDefinition = "VARCHAR(255) DEFAULT NULL COMMENT '合并账号集合（仅合并业务）'")
	private String hbzh;
	@Column(name = "XingMing", columnDefinition = "VARCHAR(120) DEFAULT NULL COMMENT '姓名'")
	private String xingMing;
	@Column(name = "ZJLX", columnDefinition = "VARCHAR(2) DEFAULT NULL COMMENT '证件类型'")
	private String zjlx;
	@Column(name = "ZJHM", columnDefinition = "VARCHAR(18) DEFAULT NULL COMMENT '证件号码'")
	private String zjhm;
	@OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@JoinColumn(name = "GRYWMX", columnDefinition = "VARCHAR(32) DEFAULT NULL COMMENT '个人业务明细'")
	private StCollectionPersonalBusinessDetails grywmx;

	public String getYwlsh() {
		return ywlsh;
	}

	public void setYwlsh(String ywlsh) {
		this.updated_at = new Date();
		this.ywlsh = ywlsh;
	}

	public String getGrzh() {
		return grzh;
	}

	public void setGrzh(String grzh) {
		this.updated_at = new Date();
		this.grzh = grzh;
	}

	public String getDwzh() {
		return dwzh;
	}

	public void setDwzh(String dwzh) {
		this.updated_at = new Date();
		this.dwzh = dwzh;
	}

	// public String getYwzt() {
	// return ywzt;
	// }
	//
	// public void setYwzt(String ywzt) {
	// this.updated_at = new Date();
	// this.ywzt = ywzt;
	// }
	//
	// public String getStep() {
	// return step;
	// }
	//
	// public void setStep(String step) {
	// this.updated_at = new Date();
	// this.step = step;
	// }

	public String getBlzl() {
		return blzl;
	}

	public void setBlzl(String blzl) {
		this.updated_at = new Date();
		this.blzl = blzl;
	}

	public String getCzmc() {
		return czmc;
	}

	public void setCzmc(String czmc) {
		this.updated_at = new Date();
		this.czmc = czmc;
	}

	public Date getSlsj() {
		return slsj;
	}

	public void setSlsj(Date slsj) {
		this.updated_at = new Date();
		this.slsj = slsj;
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

	public String getBlzh() {
		return blzh;
	}

	public void setBlzh(String blzh) {
		this.updated_at = new Date();
		this.blzh = blzh;
	}

	public String getHbzh() {
		return hbzh;
	}

	public void setHbzh(String hbzh) {
		this.updated_at = new Date();
		this.hbzh = hbzh;
	}

	public String getXingMing() {
		return xingMing;
	}

	public void setXingMing(String xingMing) {
		this.updated_at = new Date();
		this.xingMing = xingMing;
	}

	public String getZjlx() {
		return zjlx;
	}

	public void setZjlx(String zjlx) {
		this.updated_at = new Date();
		this.zjlx = zjlx;
	}

	public String getZjhm() {
		return zjhm;
	}

	public void setZjhm(String zjhm) {
		this.updated_at = new Date();
		this.zjhm = zjhm;
	}

	public StCollectionPersonalBusinessDetails getGrywmx() {
		return grywmx;
	}

	public void setGrywmx(StCollectionPersonalBusinessDetails grywmx) {
		this.updated_at = new Date();
		this.grywmx = grywmx;
	}

	public CCollectionIndividualAccountMergeVice() {
		super();

	}

	public CCollectionIndividualAccountMergeVice(String id, Date created_at, Date updated_at, Date deleted_at,
			boolean deleted, String ywlsh, String grzh, String dwzh,
			/** String ywzt, String step, **/
			String blzl, String czmc, Date slsj, String czy, String ywwd, String blzh, String hbzh, String xingMing, String zjlx, String zjhm, StCollectionPersonalBusinessDetails grywmx) {
		this.id = id;
		this.created_at = created_at;
		this.updated_at = updated_at;
		this.deleted_at = deleted_at;
		this.deleted = deleted;
		this.ywlsh = ywlsh;
		this.grzh = grzh;
		this.dwzh = dwzh;
		// this.ywzt = ywzt;
		// this.step = step;
		this.blzl = blzl;
		this.czmc = czmc;
		this.slsj = slsj;
		this.czy = czy;
		this.ywwd = ywwd;
		this.blzh = blzh;
		this.hbzh = hbzh;
		this.xingMing = xingMing;
		this.zjlx = zjlx;
		this.zjhm = zjhm;
		this.grywmx = grywmx;
	}

}
