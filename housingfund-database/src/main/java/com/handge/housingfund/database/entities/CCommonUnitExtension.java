package com.handge.housingfund.database.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "c_common_unit_extension")
@org.hibernate.annotations.Table(appliesTo = "c_common_unit_extension", comment = "缴存单位信息扩展表")
public class CCommonUnitExtension extends Common implements Serializable {

	private static final long serialVersionUID = 3676002774744738133L;

	@Column(name = "DWLB", columnDefinition = "VARCHAR(10) DEFAULT NULL COMMENT '单位类别'")
	private String dwlb;
	@Column(name = "KGQK", columnDefinition = "TEXT DEFAULT NULL COMMENT '控股情况'")
	private String kgqk;
	@Column(name = "DWXZQY", columnDefinition = "VARCHAR(10) DEFAULT NULL COMMENT '单位行政区域'")
	private String dwxzqy;
	@Column(name = "PZJGMC", columnDefinition = "VARCHAR(255) DEFAULT NULL COMMENT '批准机关名称'")
	private String pzjgmc;
	@Column(name = "PZJGJB", columnDefinition = "VARCHAR(10) DEFAULT NULL COMMENT '批准机关级别'")
	private String pzjgjb;
	@Column(name = "DJZCH", columnDefinition = "VARCHAR(20) DEFAULT NULL COMMENT '登记注册号'")
	private String djzch;
	@Column(name = "DJSYYZ", columnDefinition = "VARCHAR(255) DEFAULT NULL COMMENT '登记使用印章'")
	private String djsyyz;
	@Column(name = "DWLXDH", columnDefinition = "VARCHAR(20) DEFAULT NULL COMMENT '单位联系电话'")
	private String dwlxdh;
	@Column(name = "DWSCHJNY", columnDefinition = "VARCHAR(6) DEFAULT NULL COMMENT '单位首次汇缴年月'")
	private String dwschjny;
	@Column(name = "BeiZhu", columnDefinition = "TEXT DEFAULT NULL COMMENT '备注'")
	private String beiZhu;
	@Column(name = "CZHM", columnDefinition = "VARCHAR(50) DEFAULT NULL COMMENT '传真号码'")
	private String czhm;
	@Column(name = "DWZL", columnDefinition = "TEXT DEFAULT NULL COMMENT '单位基础资料'")
	private String dwzl;
	@Column(name = "KHWD", columnDefinition = "VARCHAR(32) DEFAULT NULL COMMENT '开户网点'")
	private String khwd;

	public String getId() {
		return id;
	}

	public void setId(String id) {

		this.id = id;
	}

	public String getKhwd() {
		return khwd;
	}

	public void setKhwd(String khwd) {
		this.khwd = khwd;
	}

	public String getDwlb() {
		return dwlb;
	}

	public void setDwlb(String dwlb) {

		this.dwlb = dwlb;
	}

	public String getKgqk() {
		return kgqk;
	}

	public void setKgqk(String kgqk) {

		this.kgqk = kgqk;
	}

	public String getDwxzqy() {
		return dwxzqy;
	}

	public void setDwxzqy(String dwxzqy) {

		this.dwxzqy = dwxzqy;
	}

	public String getPzjgmc() {
		return pzjgmc;
	}

	public void setPzjgmc(String pzjgmc) {

		this.pzjgmc = pzjgmc;
	}

	public String getPzjgjb() {
		return pzjgjb;
	}

	public void setPzjgjb(String pzjgjb) {

		this.pzjgjb = pzjgjb;
	}

	public String getDjzch() {
		return djzch;
	}

	public void setDjzch(String djzch) {

		this.djzch = djzch;
	}

	public String getDjsyyz() {
		return djsyyz;
	}

	public void setDjsyyz(String djsyyz) {

		this.djsyyz = djsyyz;
	}

	public String getDwlxdh() {
		return dwlxdh;
	}

	public void setDwlxdh(String dwlxdh) {

		this.dwlxdh = dwlxdh;
	}

	public String getDwschjny() {
		return dwschjny;
	}

	public String getBeiZhu() {
		return beiZhu;
	}

	public void setBeiZhu(String beiZhu) {

		this.beiZhu = beiZhu;
	}

	public String getCzhm() {
		return czhm;
	}

	public void setCzhm(String czhm) {

		this.czhm = czhm;
	}

	public void setDwschjny(String dwschjny) {

		this.dwschjny = dwschjny;
	}

	public String getDwzl() {
		return dwzl;
	}

	public void setDwzl(String dwzl) {
		this.dwzl = dwzl;
	}

	public CCommonUnitExtension() {
		super();

	}

	public CCommonUnitExtension(String id, Date created_at, Date updated_at, Date deleted_at, boolean deleted, String dwlb, String kgqk, String dwxzqy, String pzjgmc, String pzjgjb,
								String djzch, String djsyyz, String dwlxdh, String dwschjny, String beiZhu, String czhm, String dwzl, String khwd) {
		this.id = id;
		this.created_at = created_at;
		this.updated_at = updated_at;
		this.deleted_at = deleted_at;
		this.deleted = deleted;
		this.dwlb = dwlb;
		this.kgqk = kgqk;
		this.dwxzqy = dwxzqy;
		this.pzjgmc = pzjgmc;
		this.pzjgjb = pzjgjb;
		this.djzch = djzch;
		this.djsyyz = djsyyz;
		this.dwlxdh = dwlxdh;
		this.dwschjny = dwschjny;
		this.beiZhu = beiZhu;
		this.czhm = czhm;
		this.dwzl = dwzl;
		this.khwd = khwd;
	}
}
