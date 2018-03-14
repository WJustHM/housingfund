package com.handge.housingfund.database.entities;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * Created by Liujuhao on 2017/7/10.
 */
@Entity
@Table(name = "c_collection_unit_information_action_vice")
@org.hibernate.annotations.Table(appliesTo = "c_collection_unit_information_action_vice", comment = "")
public class CCollectionUnitInformationActionVice extends Common implements Serializable {
	private static final long serialVersionUID = -5280141486336350126L;
	@Column(name = "YWLSH", columnDefinition = "VARCHAR(20) DEFAULT NULL COMMENT '业务流水号'")
	private String ywlsh;
	@Column(name = "DWZH", columnDefinition = "VARCHAR(20) DEFAULT NULL COMMENT '单位账号'")
	private String dwzh;
	@Column(name = "CZMC", columnDefinition = "VARCHAR(2) DEFAULT NULL COMMENT '操作名称'")
	private String czmc;
	@Column(name = "FCHXHYY", columnDefinition = "VARCHAR(2) DEFAULT NULL COMMENT '封存或销户原因'")
	private String fchxhyy;
	@Column(name = "QTCZYY", columnDefinition = "VARCHAR(255) DEFAULT NULL COMMENT '其他操作原因（如启封等等）'")
	private String qtczyy;
	@Column(name = "BeiZhu", columnDefinition = "TEXT DEFAULT NULL COMMENT '备注'")
	private String beiZhu;
	@Column(name = "BLZL", columnDefinition = "TEXT DEFAULT NULL COMMENT '办理资料'")
	private String blzl;
	@Column(name = "YWWD", columnDefinition = "VARCHAR(255) DEFAULT NULL COMMENT '业务网点'")
	private String ywwd;
	@Column(name = "SLSJ", columnDefinition = "DATETIME DEFAULT NULL COMMENT '受理时间'")
	private Date slsj;
	// @Column(name = "YWZT", columnDefinition = "char(2) DEFAULT NULL COMMENT
	// '业务状态'")
	// private String ywzt;
	@Column(name = "CZY", columnDefinition = "VARCHAR(120) DEFAULT NULL COMMENT '操作员'")
	private String czy;
	@Column(name = "DWLB", columnDefinition = "VARCHAR(2) DEFAULT NULL COMMENT '单位类别'")
	private String dwlb;
	// @Column(name = "STEP", columnDefinition = "char(2) DEFAULT NULL COMMENT
	// '审核状态'")
	// private String step;
	@Column(name = "DWXHYY", columnDefinition = "VARCHAR(2) DEFAULT NULL COMMENT '单位销户原因 附A.3'")
	private String dwxhyy;
	@OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@JoinColumn(name = "DWYWMX", columnDefinition = "VARCHAR(32) DEFAULT NULL COMMENT '单位业务明细'")
	private StCollectionUnitBusinessDetails dwywmx;

	public String getYwlsh() {
		return ywlsh;
	}

	public void setYwlsh(String ywlsh) {
		this.updated_at = new Date();
		this.ywlsh = ywlsh;
	}

	public String getDwzh() {
		return dwzh;
	}

	public void setDwzh(String dwzh) {
		this.updated_at = new Date();
		this.dwzh = dwzh;
	}

	public String getCzmc() {
		return czmc;
	}

	public void setCzmc(String czmc) {
		this.updated_at = new Date();
		this.czmc = czmc;
	}

	public String getFchxhyy() {
		return fchxhyy;
	}

	public void setFchxhyy(String czyy) {
		this.updated_at = new Date();
		this.fchxhyy = czyy;
	}

	public String getQtczyy() {
		return qtczyy;
	}

	public void setQtczyy(String qtczyy) {
		this.updated_at = new Date();
		this.qtczyy = qtczyy;
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

	public String getYwwd() {
		return ywwd;
	}

	public void setYwwd(String ywwd) {
		this.updated_at = new Date();
		this.ywwd = ywwd;
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

	public String getDwlb() {
		return dwlb;
	}

	public void setDwlb(String dwlb) {
		this.updated_at = new Date();
		this.dwlb = dwlb;
	}

	public String getDwxhyy() {
		return dwxhyy;
	}

	public void setDwxhyy(String dwxhyy) {
		this.updated_at = new Date();
		this.dwxhyy = dwxhyy;
	}

	public StCollectionUnitBusinessDetails getDwywmx() {
		return dwywmx;
	}

	public void setDwywmx(StCollectionUnitBusinessDetails dwywmx) {
		this.updated_at = new Date();
		this.dwywmx = dwywmx;
	}

	public CCollectionUnitInformationActionVice() {
		super();

	}

	public CCollectionUnitInformationActionVice(String id, Date created_at, Date updated_at, Date deleted_at,
			boolean deleted, String ywlsh, String dwzh, String czmc, String fchxhyy, String qtczyy, String beiZhu,
			String blzl, String ywwd, Date slsj, /** String ywzt, **/
			String czy, String dwlb, /** String step, **/
			String dwxhyy, StCollectionUnitBusinessDetails dwywmx) {
		this.id = id;
		this.created_at = created_at;
		this.updated_at = updated_at;
		this.deleted_at = deleted_at;
		this.deleted = deleted;
		this.ywlsh = ywlsh;
		this.dwzh = dwzh;
		this.czmc = czmc;
		this.fchxhyy = fchxhyy;
		this.qtczyy = qtczyy;
		this.beiZhu = beiZhu;
		this.blzl = blzl;
		this.ywwd = ywwd;
		this.slsj = slsj;
		// this.ywzt = ywzt;
		this.czy = czy;
		this.dwlb = dwlb;
		// this.step = step;
		this.dwxhyy = dwxhyy;
		this.dwywmx = dwywmx;
	}

}
